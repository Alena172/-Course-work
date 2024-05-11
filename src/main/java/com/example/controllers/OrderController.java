package com.example.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.entities.*;
import com.example.services.ProductService;
import com.example.services.OrderService;
import com.example.services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Controller
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender emailSender;

    private static final int DEFAULT_NUMBER_OF_ITEMS = 1;

    private void sendOrderConfirmationEmail(String email, Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение заказа");
        message.setText("Ваш заказ на сумму " + order.getTotalCost() + "₽ успешно создан. Спасибо за покупку!");
        emailSender.send(message);
    }

    @PostMapping("/cartPost")
    public String getNumberOfItems(@RequestParam("numberOfItems") Integer numberOfItems, HttpSession session) {
        if (numberOfItems == null || numberOfItems <= 0) {
            numberOfItems = DEFAULT_NUMBER_OF_ITEMS;
        }
        session.setAttribute("numberOfItems", numberOfItems);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String getShoppingCart(Model model, HttpSession session) {
        Integer numberOfItems = (Integer) session.getAttribute("numberOfItems");
        if (numberOfItems == null || numberOfItems <= 0) {
            return "redirect:/emptyCart";
        }
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);
        for (int i = 0; i < numberOfItems; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(new Product());
            order.addOrderItem(orderItem);
        }
        model.addAttribute("order", order);
        return "cart";
    }


    @GetMapping("/emptyCart")
    public String showEmptyCartPage(Model model) {
        model.addAttribute("numberOfItems", DEFAULT_NUMBER_OF_ITEMS);
        return "emptyCart";
    }

    @PostMapping("/add-order")
    public String addNewOrder(@ModelAttribute Order order, @ModelAttribute OrderItem orderItems, @RequestParam("totalCost") Integer totalCost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        order.setDate(currentDate.format(formatter));
        order.setUser(user);
        order.setStatus("Принят в обработку");
        order.setTotalCost(totalCost);

        for (OrderItem orderItem : order.getOrderItems()) {
            orderService.saveOrderItem(orderItem);
        }
        orderService.saveOrder(order);
        sendOrderConfirmationEmail(user.getUsername(), order);
        return "redirect:orders";
    }

    @GetMapping("/")
    public String getStart() {
        return "redirect:menu";
    }

    @GetMapping("/menu")
    public String getCatalog(Model model) {
        model.addAttribute("numberOfItems", DEFAULT_NUMBER_OF_ITEMS);
        model.addAttribute("productList", productService.findAll());
        return "menu";
    }

    @GetMapping("/orders")
    public String getAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();
        model.addAttribute("numberOfItems", DEFAULT_NUMBER_OF_ITEMS);
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersByUserId(userId));
        return "orders";
    }
}

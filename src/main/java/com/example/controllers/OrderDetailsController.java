package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.entities.Order;
import com.example.services.OrderService;

import java.util.Optional;

@Controller
public class OrderDetailsController {

    private final OrderService orderService;

    public OrderDetailsController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orderDetails")
    public String getOrderDetails(@RequestParam int id, Model model) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        model.addAttribute("order", optionalOrder.get());
        return "orderDetails";
    }

}

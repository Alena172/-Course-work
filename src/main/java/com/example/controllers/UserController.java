package com.example.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.entities.User;
import com.example.services.UserService;

@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("login")
    public String getRegistration(Model model) {
        model.addAttribute("userReg", new User());
        return "enter";
    }

    @PostMapping("reg")
    public String addNewUser(@ModelAttribute("userReg") User user) {
        userService.addUser(user);
        return "redirect:login";
    }

    @GetMapping("/myaccount")
    public String getMyAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "myaccount";
    }
}
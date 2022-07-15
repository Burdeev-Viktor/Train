package com.example.Train.controller;

import com.example.Train.model.User;
import com.example.Train.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {


    private final UserService userService;


    public RegistrationController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (!userService.createNewUser(user, model)) {
            return "registration";
        }
        return "redirect:/login";
    }
}

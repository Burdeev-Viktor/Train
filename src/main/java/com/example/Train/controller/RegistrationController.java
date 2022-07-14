package com.example.Train.controller;

import com.example.Train.model.Role;
import com.example.Train.model.User;
import com.example.Train.model.User_Role;
import com.example.Train.repository.UserRepository;
import com.example.Train.repository.UserRoleRepository;
import com.example.Train.service.UserRoleService;
import com.example.Train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromdb = userService.findUserByUsername(user.getUsername());
        if (userFromdb != null) {
            model.addAttribute("massageUserIsExist", "Такой пользователь существует!");
            return "registration";
        }
        if (!Objects.equals(user.getPassword(), user.getConfPassword())) {
            model.addAttribute("massagePasswordsNotMatch", "Пороли не совпадают");
            return "registration";
        }
        user.setWallet(0.0f);
        userService.add(user);
        user = userService.findUserByUsername(user.getUsername());
        userRoleService.save(new User_Role(user.getId(), 2));
        return "redirect:/login";
    }
}

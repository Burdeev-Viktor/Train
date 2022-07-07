package com.example.Train.controller;

import com.example.Train.model.User;
import com.example.Train.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
        User userFromdb = userRepository.findByUsername(user.getUsername());
        if(userFromdb!=null ){
            model.addAttribute("massege","User Exist!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(1);
        userRepository.save(user);
        return "redirect:/login";
    }
}

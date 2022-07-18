package com.example.Train.controller;

import com.example.Train.Calculation;
import com.example.Train.model.Train;
import com.example.Train.model.User;
import com.example.Train.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TrainController {
    private final TrainService trainService;
    private final TicketService ticketService;
    private final UserService userService;
    private List<Train> trainList=new ArrayList<>();
    private boolean sort = false;

    @Autowired
    public TrainController(TrainService trainService, TicketService ticketService, UserRoleService userRoleService, RoleService roleService, UserService userService) {
        this.trainService = trainService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/sort-by-price")
    public String SortByPrice(@AuthenticationPrincipal UserDetails userDetails, Model model, Train train) {
        sort = true;
        Calculation.formatDate(train);
        model.addAttribute("trainList", Calculation.sortByPrice(trainList, sort));
        model.addAttribute("trainSearch", train);
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "trains";
    }

    @PostMapping("/train-search")
    public String search(@AuthenticationPrincipal UserDetails userDetails, Train train, Model model) {
        Calculation.formatDate(train);
        trainList = Calculation.sortByTrain(train, trainService.getAll());
        model.addAttribute("trainList", trainList);
        model.addAttribute("trainSearch", train);
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "trains";
    }

    @GetMapping("/trains")
    public String findAll(@AuthenticationPrincipal UserDetails userDetails, Model model, Train train) {
        if (userService.userRolePageMapping(userDetails.getUsername())) {
            return "redirect:/admin-page";
        }
        trainList = trainService.getAll();
        Calculation.sortByPrice(trainList, sort);
        model.addAttribute("trainList", trainList);
        model.addAttribute("trainSearch", new Train());
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "trains";
    }

    @GetMapping("train-buy/{id}")
    public String buyTrain(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, Model model) {
        model.addAttribute("train", trainService.getTrain(id));
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "buy-ticket";
    }

    @GetMapping("/buy-ticket/{id}")
    public String buyTicket(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, Model model) {
        if (userService.userBuyTicket(userDetails.getUsername(), id)) {
            return "redirect:/trains";
        } else {
            model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
            model.addAttribute("train", trainService.getTrain(id));
            model.addAttribute("notMoney", "нехватает денег");
        }
        return "buy-ticket";
    }


    @GetMapping("/refill-balance")
    public String refillBalanceForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        model.addAttribute("wallet", new User());
        return "refill-balance-page";
    }

    @PostMapping("/refill-balance")
    public String refillBalance(@AuthenticationPrincipal UserDetails userDetails, User wallet, Model model) {
        userService.refillBalance(userDetails.getUsername(), wallet.getWallet());
        return "redirect:/trains";
    }

    @GetMapping("/tickets")
    public String ticketsForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("list_ticket", ticketService.findByUsername(userDetails.getUsername()));
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "tickets";
    }

}

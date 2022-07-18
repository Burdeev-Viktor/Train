package com.example.Train.controller;

import com.example.Train.Calculation;
import com.example.Train.model.Train;
import com.example.Train.model.User;
import com.example.Train.service.TicketService;
import com.example.Train.service.TrainService;
import com.example.Train.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin-page")

public class AdminController {

    private final TrainService trainService;
    private final TicketService ticketService;

    private final UserService userService;
    private List<Train> trainList= new ArrayList<>();
    private boolean sort = false;

    public AdminController(TrainService trainService, TicketService ticketService, UserService userService) {
        this.trainService = trainService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal UserDetails userDetails, Model model, Train train) {
        trainList = trainService.getAll();
        model.addAttribute("trainList", Calculation.sortByPrice(trainList, sort));
        model.addAttribute("trainSearch", new Train());
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "admin-page";
    }

    @PostMapping("/train-search")
    public String search(@AuthenticationPrincipal UserDetails userDetails, Train train, Model model) {
        Calculation.formatDate(train);
        trainList = Calculation.sortByTrain(train, trainService.getAll());
        model.addAttribute("trainList", Calculation.sortByPrice(trainList, sort));
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "admin-page";
    }

    @GetMapping("train-delete/{id}")
    public String buyTrain(@PathVariable("id") Long id) {
        trainService.deleteById(id);
        return "redirect:/trains";
    }

    @GetMapping("/train-update/{id}")
    public String trainUpdateForm(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, Model model) {
        Train train = trainService.getTrain(id);
        Calculation.deFormatDate(train);
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        model.addAttribute("train", train);
        return "train-update";
    }

    @PostMapping("/train-update")
    public String createUpdate(Train train) {
        trainService.saveNewTrain(train);
        return "redirect:/admin-page";
    }

    @GetMapping("train-buy/{id}")
    public String buyTrain(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        model.addAttribute("train", trainService.getTrain(id));
        return "buy-ticket";
    }

    @GetMapping("/buy-ticket/{id}")
    public String buyTicket(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, Model model) {
        if (userService.userBuyTicket(userDetails.getUsername(), id)) {
            return "redirect:/admin-page";
        } else {
            model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
            model.addAttribute("train", trainService.getTrain(id));
            model.addAttribute("notMoney", "нехватает денег");
        }
        return "buy-ticket";
    }

    @GetMapping("/train-create")
    public String trainCreateForm(@AuthenticationPrincipal UserDetails userDetails, Train train, Model model) {
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "train-create";
    }

    @PostMapping("/train-create")
    public String createTrain(Train train, Model model) {
        if (!train.trainIsExists()) {
            model.addAttribute("massageFilledAll", "Заполните все поля");
            return "train-create";
        }
        trainService.saveNewTrain(train);
        return "redirect:/admin-page";
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
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "redirect:/admin-page";
    }

    @GetMapping("/sort-by-price")
    public String SortByPrice(@AuthenticationPrincipal UserDetails userDetails, Model model, Train train) {
        sort = true;
        model.addAttribute("trainList", Calculation.sortByPrice(trainList, sort));
        model.addAttribute("train", train);
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "admin-page";
    }

    @GetMapping("/tickets")
    public String ticketsForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("list_ticket", ticketService.findByUsername(userDetails.getUsername()));
        model.addAttribute("user", userService.findUserByUsername(userDetails.getUsername()));
        return "tickets";
    }

    @GetMapping("/reset")
    public String resetForm() {
        sort = false;
        return "redirect:/admin-page";
    }


}

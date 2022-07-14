package com.example.Train.controller;

import com.example.Train.Calculation;
import com.example.Train.model.Train;
import com.example.Train.model.User;
import com.example.Train.model.User_Role;
import com.example.Train.service.TicketService;
import com.example.Train.service.TrainService;
import com.example.Train.service.UserRoleService;
import com.example.Train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;


@Controller
public class TrainController {
private final TrainService trainService;
@Autowired
private TicketService ticketService;

    @Autowired
    private UserRoleService userRoleService;

@Autowired
private UserService userService;
private List<Train> trainList;
private User user;
private boolean sort=false;

@Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping   ("/sort-by-price")
    public String SortByPrice(Model model,Train train){
        if(train.dateIsExists()) {
            Calculation.formatDate(train);
        }
        trainList=trainService.getAll();
        if(sort){
            Calculation.sortByPrice(trainList);
        }
        model.addAttribute("trainList",Calculation.sortByTrain(train,trainList));
        model.addAttribute("user",user);
        return "trains";
    }
    @PostMapping("/train-search")
    public String search(Train train, Model model){
    if(train.dateIsExists()) {
        Calculation.formatDate(train);
    }
        trainList=trainService.getAll();
        trainList = Calculation.sortByTrain(train,trainList);
        model.addAttribute("trainList",trainList);
        model.addAttribute("user",user);
        return "trains";
    }
    @GetMapping("/trains")
    public String findAll(@AuthenticationPrincipal UserDetails userDetails,Model model,Train train){
        user = userService.findUserByUsername(userDetails.getUsername());
        User_Role role = userRoleService.findByUser_Id(user.getId());
        if(role.getRole_id()==1){
            return "redirect:/admin-page";
        }
        trainList = trainService.getAll();
        Train trainSearch = new Train();
        model.addAttribute("trainList",trainList);
        model.addAttribute("trainSearch",trainSearch);
        model.addAttribute("user",user);
    return "trains";
    }
@GetMapping("train-buy/{id}")
    public String buyTrain( @PathVariable("id") Long id, Model model){
    Train train = trainService.getTrain(id);
    model.addAttribute("train",train);
    model.addAttribute("user",user);
    return "buy-ticket";
    }
    @GetMapping("/buy-ticket/{id}")
    public String buyTicket(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, Model model){

        Train train = trainService.getTrain(id);

    User user = userService.findUserByUsername(userDetails.getUsername());
    if(train.getPrice()<=user.getWallet()){
        ticketService.saveByTrainUser(train, userDetails.getUsername());
        train.setSeat_buy(train.getSeat_buy()+1);
        trainService.saveTrain(train);
        model.addAttribute("train",train);

        user.setWallet(user.getWallet()- train.getPrice());

        userService.save(user);
        return "redirect:/trains";
    } else {
        model.addAttribute("user",user);
        model.addAttribute("train",train);
        model.addAttribute("notMoney","нехватает денег");
    }


        return "buy-ticket";
    }


    @GetMapping("/refill-balance")
    public String refillBalanceForm(Model model){
    model.addAttribute("user",user);
        model.addAttribute("wallet",new User());
    return "refill-balance-page";
    }
    @PostMapping("/refill-balance")
    public String refillBalance(User wallet,Model model){
        user.setWallet(user.getWallet()+Calculation.rounding(wallet.getWallet()));
        userService.save(user);
        User_Role role = userRoleService.findByUser_Id(user.getId());
        if(role.getRole_id()==1){
            return "redirect:/admin-page";
        }
        return "redirect:/trains";
    }

    @GetMapping("/tickets")
    public String ticketsForm(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("list_ticket",ticketService.findByUsername(userDetails.getUsername()));
        model.addAttribute("user",user);
    return "tickets";
    }
    @GetMapping("/reset")
    public String resetForm(){
        sort=false;
        return "redirect:/trains";
    }

}

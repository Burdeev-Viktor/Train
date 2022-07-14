package com.example.Train.controller;

import com.example.Train.Calculation;
import com.example.Train.model.Train;
import com.example.Train.model.User;
import com.example.Train.service.TicketService;
import com.example.Train.service.TrainService;
import com.example.Train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/admin-page")

public class AdminController {

        @Autowired
        private TrainService trainService;
        @Autowired
        private TicketService ticketService;

        @Autowired
        private UserService userService;
        private List<Train> trainList;
        private User user;
    private boolean sort = false;
    @GetMapping
    public String adminPage(@AuthenticationPrincipal UserDetails userDetails,Model model,Train train){
        user = userService.findUserByUsername(userDetails.getUsername());
        trainList = trainService.getAll();
        Train trainSearch = new Train();
        if(sort){
            Calculation.sortByPrice(trainList);
        }
        model.addAttribute("trainList",trainList);
        model.addAttribute("trainSearch",trainSearch);
        model.addAttribute("user",user);
        return "admin-page";
    }
    @PostMapping("/train-search")
    public String search(Train train, Model model){
        if(train.dateIsExists()) {
            Calculation.formatDate(train);
        }
        trainList=trainService.getAll();
        if(sort){
            Calculation.sortByPrice(trainList);
        }
        model.addAttribute("trainList",Calculation.sortByTrain(train,trainList));
        model.addAttribute("user",user);
        return "admin-page";
    }
    @GetMapping("train-delete/{id}")
    public String buyTrain(@PathVariable("id") Long id){
        trainService.deleteById(id);
        return "redirect:/trains";
    }
    @GetMapping("/train-update/{id}")
    public String trainUpdateForm(@PathVariable("id") Long id , Model model){
        Train train = trainService.getTrain(id);
        train.setDate( train.getDate().substring(6,10)+"-"+train.getDate().substring(3 ,5)+"-"+train.getDate().substring(0,2));

        model.addAttribute("train",train);

        return "train-update";
    }
    @PostMapping("/train-update")
    public String createUpdate(Train train){
        train.setTimeOfTrack(Calculation.timeToTrack(train.getTimeStart(), train.getTimeEnd()));
        train.setDate( train.getDate().substring(8,10)+"-"+train.getDate().substring(5,7)+"-"+train.getDate().substring(0,4));
        trainService.saveTrain(train);
        return "redirect:/admin-page";
    }
    @GetMapping("train-buy/{id}")
    public String buyTrain( @PathVariable("id") Long id, Model model){
        Train train = trainService.getTrain(id);
        model.addAttribute("train",train);
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
            return "redirect:/admin-page";
        } else {
            model.addAttribute("train",train);
            model.addAttribute("notMoney","нехватает денег");
        }


        return "buy-ticket";
    }

    @GetMapping("/train-create")
    public String trainCreateForm(Train train){
        return "train-create";
    }
    @PostMapping("/train-create")
    public String createTrain(Train train,Model model){
if(!train.trainIsExists()){
    model.addAttribute("massageFilledAll","Заполните все поля");
    return "train-create";
}
        train.setTimeOfTrack(Calculation.timeToTrack(train.getTimeStart(), train.getTimeEnd()));
        train.setDate( train.getDate().substring(8,10)+"-"+train.getDate().substring(5,7)+"-"+train.getDate().substring(0,4));
        trainService.saveTrain(train);
        return "redirect:/admin-page";
    }
    @GetMapping("/refill-balance")
    public String refillBalanceForm(Model model){
        model.addAttribute("user",user);
        Float wallet=0.0f;
        model.addAttribute("wallet",wallet);
        return "refill-balance-page";
    }
    @PostMapping("/refill-balance")
    public String refillBalance(User wallet,Model model){
        user.setWallet(user.getWallet()+wallet.getWallet());
        userService.save(user);
        model.addAttribute("user",user);
        return "redirect:/admin-page";
    }
    @GetMapping   ("/sort-by-price")
    public String SortByPrice(Model model,Train train){
        sort=true;
        trainList=Calculation.sortByPrice(trainList);

        model.addAttribute("trainList",trainList);
        model.addAttribute("train",train);
        model.addAttribute("user",user);
        return "admin-page";
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
        return "redirect:/admin-page";
    }







}

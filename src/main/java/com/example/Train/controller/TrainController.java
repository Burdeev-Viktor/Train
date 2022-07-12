package com.example.Train.controller;

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

import java.util.*;


@Controller
public class TrainController {
private final TrainService trainService;
@Autowired
private TicketService ticketService;

@Autowired
private UserService userService;
private List<Train> trainList;
private User user;

@Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping   ("/sort-by-price")
    public String SortByPrice(Model model,Train train){
        Collections.sort(trainList, new Comparator<Train>() {
            @Override
            public int compare(Train o1, Train o2) {
                return o1.getPrice()- o2.getPrice();
            }
        });

        model.addAttribute("trainList",trainList);
        model.addAttribute("train",train);
        model.addAttribute("user",user);
        return "trains";
    }
    @PostMapping("/train-search")
    public String search(Train train, Model model){
    if(train.getDayOfWeek()!=null && !Objects.equals(train.getDayOfWeek(), "")) {
        train.setDayOfWeek(train.getDayOfWeek().substring(8, 10) + "-" + train.getDayOfWeek().substring(5, 7) + "-" + train.getDayOfWeek().substring(0, 4));
    }
        trainList = searchByTrain(train);
        model.addAttribute("trainList",trainList);
        return "trains";
    }
    private List<Train> searchByTrain(Train trainSearh){
    trainList = trainService.getAll();
        List<Train> sortlist = new ArrayList<Train>();
        for (Train train : trainList) {
            if ((Objects.equals(train.getStart(), trainSearh.getStart())
                    || Objects.equals(trainSearh.getStart(), "")) && (Objects.equals(train.getEnd(), trainSearh.getEnd())) && ((Objects.equals(train.getDayOfWeek(), trainSearh.getDayOfWeek()))
                    || Objects.equals(trainSearh.getDayOfWeek(), "")) || (Objects.equals(train.getStart(), trainSearh.getStart())
                    || Objects.equals(trainSearh.getStart(), "")) && Objects.equals(trainSearh.getEnd(), "") && ((Objects.equals(train.getDayOfWeek(), trainSearh.getDayOfWeek()))
                    || Objects.equals(trainSearh.getDayOfWeek(), ""))) {
                sortlist.add(train);
            }
        }
        return sortlist;
    }
    @GetMapping("/trains")
    public String findAll(@AuthenticationPrincipal UserDetails userDetails,Model model,Train train){
        user = userService.findUserByUsername(userDetails.getUsername());
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
        model.addAttribute("train",train);
        model.addAttribute("notMoney","нехватает денег");
    }


        return "buy-ticket";
    }

    @GetMapping("train-delete/{id}")
    public String buyTrain(@PathVariable("id") Long id){
        trainService.deleteById(id);
        return "redirect:/trains";
    }
    @GetMapping("/train-update/{id}")
    public String trainUpdateForm(@PathVariable("id") Long id ,Model model){
        Train train = trainService.getTrain(id);
        model.addAttribute("train",train);
        return "train-update";
    }
    @PostMapping("/train-update")
    public String createUpdate(Train train){
        train.setTimeOfTrack(timeToTrack(train.getTimeStart(), train.getTimeEnd()));
        train.setDayOfWeek( train.getDayOfWeek().substring(8,10)+"-"+train.getDayOfWeek().substring(5,7)+"-"+train.getDayOfWeek().substring(0,4));
        trainService.saveTrain(train);
        return "redirect:/trains";
    }
    @GetMapping("/refill-balance")
    public String refillBalanceForm(Model model){
    model.addAttribute("user",user);
        model.addAttribute("wallet",new User());
    return "refill-balance-page";
    }
    @PostMapping("/refill-balance")
    public String refillBalance(User wallet,Model model){
        user.setWallet(user.getWallet()+wallet.getWallet());
        userService.save(user);
        return "redirect:/trains";
    }

    @GetMapping("/train-create")
    public String trainCreateForm(Train train){
        return "train-create";
    }
    @PostMapping("/train-create")
    public String createTrain(Train train){
        train.setTimeOfTrack(timeToTrack(train.getTimeStart(), train.getTimeEnd()));
        train.setDayOfWeek( train.getDayOfWeek().substring(8,10)+"-"+train.getDayOfWeek().substring(5,7)+"-"+train.getDayOfWeek().substring(0,4));
        trainService.saveTrain(train);
        return "redirect:/trains";
    }
    @GetMapping("/tickets")
    public String ticketsForm(@AuthenticationPrincipal UserDetails user, Model model){
        model.addAttribute("list_ticket",ticketService.findByUsername(user.getUsername()));
    return "tickets";
    }



    public String timeToTrack(String str1,String str2){
        String str1_1 = str1.substring(0,str1.indexOf(':'));
        String str1_2 = str1.substring(str1.indexOf(':')+1);
        String str2_1 = str2.substring(0,str2.indexOf(':'));
        String str2_2 = str2.substring(str2.indexOf(':')+1);
        int min1 = Integer.parseInt(str1_1)*60+Integer.parseInt(str1_2);
        int min2 = Integer.parseInt(str2_1)*60+Integer.parseInt(str2_2);
        int res = min2-min1;
        if(res<0) {
            res = Math.abs(res);
            res += (60 * 24);
        }
        String hours ="";
        if(res/60<10){
            hours="0"+res/60;
        }else {
            hours= String.valueOf(res/60);
        }
        String min;
        if(res%60<10){
            min="0"+res%60;
        }else {
            min= String.valueOf(res%60);
        }
        return hours+":"+min;
    }
}

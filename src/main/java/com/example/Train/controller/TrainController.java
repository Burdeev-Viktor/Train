package com.example.Train.controller;

import com.example.Train.model.Train;
import com.example.Train.model.User;
import com.example.Train.service.TicketService;
import com.example.Train.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


@Controller
public class TrainController {
private final TrainService trainService;
@Autowired
private TicketService ticketService;

@Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping("/train-search")
    public String search(Train train, Model model){
        train.setDayOfWeek( train.getDayOfWeek().substring(8,10)+"-"+train.getDayOfWeek().substring(5,7)+"-"+train.getDayOfWeek().substring(0,4));
        List<Train> shortlist = searchByTrain(train);
        model.addAttribute("trainList",shortlist);
        return "trains";
    }
    private List<Train> searchByTrain(Train trainSearh){
        List<Train> trainList = trainService.getAll();
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
    public String findAll(Model model,Train train){
        List<Train> trainList;
        trainList = trainService.getAll();
        Train trainSearch = new Train();
        model.addAttribute("trainList",trainList);
        model.addAttribute("trainSearch",trainSearch);
    return "trains";
    }
@GetMapping("train-buy/{id}")
    public String buyTrain(@AuthenticationPrincipal UserDetails user, @PathVariable("id") Long id, Model model){
    Train train = trainService.getTrain(id);
    ticketService.saveByTrainUser(train, user.getUsername());
    train.setSeat_buy(train.getSeat_buy()+1);
    trainService.saveTrain(train);
    model.addAttribute("train",train);
    return "redirect:/trains  ";
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

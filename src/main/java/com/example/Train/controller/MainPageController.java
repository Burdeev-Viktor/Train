package com.example.Train.controller;

import com.example.Train.model.Train;
import com.example.Train.service.TrainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Controller
public class MainPageController {

    private final TrainService trainService;


    public MainPageController(TrainService trainService) {
        this.trainService = trainService;
    }
    @GetMapping("/")
    public String hello(Model model, Train train){
        List<Train> trainList;
        trainList = trainService.getAll();
        Train trainSearch = new Train();
        model.addAttribute("trainList",trainList);
        model.addAttribute("trainSearch",trainSearch);
        return "main_page";
    }
    @GetMapping ("/main-page")
    public String helloPage(Model model, Train train){
        List<Train> trainList;
        trainList = trainService.getAll();
        Train trainSearch = new Train();
        model.addAttribute("trainList",trainList);
        model.addAttribute("trainSearch",trainSearch);
        return "redirect:/";
    }
    @PostMapping("/train-search-guest")
    public String search(Train train, Model model){
        List<Train> shortlist = searchByTrain(train);
        model.addAttribute("trainList",shortlist);
        return "main_page";
    }
    private List<Train> searchByTrain(Train trainSearh){
        List<Train> trainList = trainService.getAll();
        if (Objects.equals(trainSearh.getDayOfWeek(), "Сегодня")){
            Calendar calendar=Calendar.getInstance();
            int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayofWeek == 1) {
                trainSearh.setDayOfWeek("Воскресенье");
            } else if (dayofWeek == 2) {
                trainSearh.setDayOfWeek("Понедельник");
            } else if (dayofWeek == 3) {
                trainSearh.setDayOfWeek("Вторник");
            } else if (dayofWeek == 4) {
                trainSearh.setDayOfWeek("Среда");
            } else if (dayofWeek == 5) {
                trainSearh.setDayOfWeek("Четверг");
            } else if (dayofWeek == 6) {
                trainSearh.setDayOfWeek("Пятница");
            } else if (dayofWeek == 7) {
                trainSearh.setDayOfWeek("Субота");
            } else {
                trainSearh.setDayOfWeek("");
            }
        }
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
}

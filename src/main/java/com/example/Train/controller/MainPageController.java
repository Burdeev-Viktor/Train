package com.example.Train.controller;

import com.example.Train.model.Train;
import com.example.Train.service.TrainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class MainPageController {

    private final TrainService trainService;
    private List<Train> trainList;

    public MainPageController(TrainService trainService) {
        this.trainService = trainService;
    }
    @GetMapping("/")
    public String hello(Model model, Train train){

        trainList = trainService.getAll();

        Train trainSearch = new Train();
        model.addAttribute("trainList",trainList);
        model.addAttribute("train",trainSearch);
        return "main_page";
    }
    @GetMapping ("/main-page")
    public String helloPage(){
        return "redirect:/";
    }
    @PostMapping("/train-search-guest")
    public String search(Train train, Model model){
        if(train.getDate()!=null && !Objects.equals(train.getDate(), "")) {
            train.setDate(train.getDate().substring(8, 10) + "-" + train.getDate().substring(5, 7) + "-" + train.getDate().substring(0, 4));
        }
        trainList = searchByTrain(train);
        model.addAttribute("trainList",trainList);
        return "main_page";
    }
    private List<Train> searchByTrain(Train trainSearch){
        List<Train> trainList = trainService.getAll();
        List<Train> sortlist = new ArrayList<Train>();
        for (Train train : trainList) {
            if ((Objects.equals(train.getStart(), trainSearch.getStart())
                    || Objects.equals(trainSearch.getStart(), "")) && (Objects.equals(train.getEnd(), trainSearch.getEnd())) && ((Objects.equals(train.getDate(), trainSearch.getDate()))
                    || Objects.equals(trainSearch.getDate(), "")) || (Objects.equals(train.getStart(), trainSearch.getStart())
                    || Objects.equals(trainSearch.getStart(), "")) && Objects.equals(trainSearch.getEnd(), "") && ((Objects.equals(train.getDate(), trainSearch.getDate()))
                    || Objects.equals(trainSearch.getDate(), ""))) {
                sortlist.add(train);
            }
        }
        return sortlist;
    }
    @GetMapping   ("/sort-by-price-guest")
    public String SortByPrice(Model model,Train train){
        Collections.sort(trainList, new Comparator<Train>() {
            @Override
            public int compare(Train o1, Train o2) {
                return o1.getPrice()- o2.getPrice();
            }
        });

        model.addAttribute("trainList",trainList);
        model.addAttribute("train",train);
        return "main_page";
    }

}

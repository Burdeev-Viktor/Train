package com.example.Train.controller;

import com.example.Train.Calculation;
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
    public String hello(Model model, Train train) {
        trainList = trainService.getAll();

        Train trainSearch = new Train();
        model.addAttribute("trainList", trainList);
        model.addAttribute("train", trainSearch);
        return "main_page";
    }

    @GetMapping("/main-page")
    public String helloPage() {
        return "redirect:/";
    }

    @PostMapping("/train-search-guest")
    public String search(Train train, Model model) {
        if (train.getDate() != null && !Objects.equals(train.getDate(), "")) {
            train.setDate(train.getDate().substring(8, 10) + "-" + train.getDate().substring(5, 7) + "-" + train.getDate().substring(0, 4));
        }
        trainList = Calculation.sortByTrain(train, trainList);
        model.addAttribute("trainList", trainList);
        return "main_page";
    }

    @GetMapping("/sort-by-price-guest")
    public String SortByPrice(Model model, Train train) {
        trainList = Calculation.sortByPrice(trainList);
        model.addAttribute("trainList", trainList);
        model.addAttribute("train", train);
        return "main_page";
    }


}

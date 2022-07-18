package com.example.Train.controller;

import com.example.Train.Calculation;
import com.example.Train.model.Train;
import com.example.Train.service.TrainService;
import com.example.Train.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainPageController {

    private final TrainService trainService;
    private List<Train> trainList=new ArrayList<>();
    private boolean sort = false;
    private final UserService userService;

    public MainPageController(TrainService trainService, UserService userService) {
        this.trainService = trainService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello(Model model, Train train) {
        userService.createAdmin();
        sort = false;
        trainList = trainService.getAll();
        model.addAttribute("trainList", trainList);
        model.addAttribute("train", train);
        return "main_page";
    }

    @GetMapping("/main-page")
    public String helloPage() {
        return "redirect:/";
    }

    @PostMapping("/train-search-guest")
    public String search(Train train, Model model) {
        Calculation.formatDate(train);
        trainList = Calculation.sortByTrain(train, trainService.getAll());
        model.addAttribute("trainList", trainList);
        return "main_page";
    }

    @GetMapping("/sort-by-price-guest")
    public String SortByPrice(Model model, Train train) {
        sort = true;
        model.addAttribute("trainList", Calculation.sortByPrice(trainList, sort));
        model.addAttribute("train", train);
        return "main_page";
    }


}

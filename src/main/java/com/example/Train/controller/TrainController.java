package com.example.Train.controller;

import com.example.Train.model.Train;
import com.example.Train.service.TrainService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }
    @GetMapping("/")
    public String hello1(){
        return "main_page";
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
    public String buyTrain(@PathVariable("id") Long id, Model model){
    Train train = trainService.getTrain(id);
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
        trainService.saveTrain(train);
        return "redirect:/trains";
    }
    @PostMapping("/train-search")
    public String searchStart(Train train, Model model){
        List<Train> sortlist = onlyStart(train);
        model.addAttribute("trainList",sortlist);
        return "trains";
    }
    private List<Train> onlyStart(Train trainSearh){
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

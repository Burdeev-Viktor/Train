package com.example.Train;

import com.example.Train.model.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calculation {
    static public void timeToTrack(Train train) {
        String str1 = train.getTimeStart();
        String str2 = train.getTimeEnd();
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

        train.setTimeOfTrack(hours + ":" + min);
    }

    static public List<Train> sortByTrain(Train trainSearch, List<Train> trainList) {
        List<Train> shortlist = new ArrayList<>();
        for (Train train : trainList) {
            if ((Objects.equals(train.getStart(), trainSearch.getStart())
                    || Objects.equals(trainSearch.getStart(), "")) && (Objects.equals(train.getEnd(), trainSearch.getEnd())) && ((Objects.equals(train.getDate(), trainSearch.getDate()))
                    || Objects.equals(trainSearch.getDate(), "")) || (Objects.equals(train.getStart(), trainSearch.getStart())
                    || Objects.equals(trainSearch.getStart(), "")) && Objects.equals(trainSearch.getEnd(), "") && ((Objects.equals(train.getDate(), trainSearch.getDate()))
                    || Objects.equals(trainSearch.getDate(), ""))) {
                shortlist.add(train);
            }
        }
        return shortlist;
    }

    static public void formatDate(Train train) {
        if (train.dateIsExists()) {
            train.setDate(train.getDate().substring(8, 10) + "-" + train.getDate().substring(5, 7) + "-" + train.getDate().substring(0, 4));
        }
    }

    static public void deFormatDate(Train train) {
        if (train.dateIsExists()) {
            train.setDate(train.getDate().substring(6, 10) + "-" + train.getDate().substring(3, 5) + "-" + train.getDate().substring(0, 2));
        }
    }

    static public List<Train> sortByPrice(List<Train> trainList, boolean sort) {
        if (sort) {
            trainList.sort((o1, o2) -> (int) (o1.getPrice() * 100 - o2.getPrice() * 100));
        }
        return trainList;
    }

    public static float rounding(Float wallet) {
        float scale = (float) Math.pow(10, 2);
        return (float) (Math.ceil(wallet * scale) / scale);
    }
}

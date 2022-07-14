package com.example.Train;

import com.example.Train.model.Train;

import java.util.*;

public class Calculation {
    static public String timeToTrack(String str1,String str2){
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

    static public List<Train> sortByTrain(Train trainSearh, List<Train> trainList){
        List<Train> sortlist = new ArrayList<Train>();
        for (Train train : trainList) {
            if ((Objects.equals(train.getStart(), trainSearh.getStart())
                    || Objects.equals(trainSearh.getStart(), "")) && (Objects.equals(train.getEnd(), trainSearh.getEnd())) && ((Objects.equals(train.getDate(), trainSearh.getDate()))
                    || Objects.equals(trainSearh.getDate(), "")) || (Objects.equals(train.getStart(), trainSearh.getStart())
                    || Objects.equals(trainSearh.getStart(), "")) && Objects.equals(trainSearh.getEnd(), "") && ((Objects.equals(train.getDate(), trainSearh.getDate()))
                    || Objects.equals(trainSearh.getDate(), ""))) {
                sortlist.add(train);
            }
        }
        return sortlist;
    }
    static public void formatDate(Train train){
        train.setDate(train.getDate().substring(8, 10) + "-" + train.getDate().substring(5, 7) + "-" + train.getDate().substring(0, 4));
    }

    static public List<Train> sortByPrice(List<Train> trainList){
        trainList.sort(new Comparator<Train>() {
            @Override
            public int compare(Train o1, Train o2) {
                return (int) (o1.getPrice()*100 - o2.getPrice()*100);
            }
        });
        return trainList;
    }
    static public float rounding(float wallet){
        float scale = (float) Math.pow(10, 2);
        return (float) (Math.ceil(wallet * scale) / scale);
    }

}

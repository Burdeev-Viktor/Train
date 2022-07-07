package com.example.Train.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Table(name = "train")
@Component
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtrain;

    @Column(name = "start")
    private String start;

    private String asd;

    @Column(name = "end")
    private String end;

    @Column(name = "seat_buy")
    private int seat_buy;

    @Column(name = "seat_all")
    private int seat_all;

    @Column(name = "price")
    private int price;

    @Column(name = "time_start")
    private String timeStart;

    @Column(name = "time_end")
    private String timeEnd;

    @Column(name = "time_of_track")
    private String timeOfTrack;

    @Column(name = "day_of_week")
    private String dayOfWeek;

}

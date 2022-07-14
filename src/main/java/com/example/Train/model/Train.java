package com.example.Train.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;

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
    private String date;

    public boolean trainIsExists(){
        return (this.start==null|| !Objects.equals(this.start, "")) &&
                (this.end==null|| !Objects.equals(this.end, ""))  &&
                (this.timeStart==null|| !Objects.equals(this.timeStart, "")) &&
                (this.timeEnd==null|| !Objects.equals(this.timeEnd, "")) &&
                (this.date==null|| !Objects.equals(this.date, ""));
    }
    public boolean dateIsExists(){
        return (date!=null && !Objects.equals(date, ""));
    }

}

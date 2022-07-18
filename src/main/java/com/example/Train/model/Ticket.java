package com.example.Train.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tickets")
@Component
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTicket;
    @Column(name = "user_name")
    private String username;
    @Column(name = "start")
    private String start;
    @Column(name = "end")
    private String end;
    @Column(name = "price")
    private float price;
    @Column(name = "time_start")
    private String timeStart;
    @Column(name = "date")
    private String date;
    @Column(name = "train")
    private Long idTrain;
    public String priceColumTicket(){
        return String.format("%.2f",price)+" руб.";
    }
}

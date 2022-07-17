package com.example.Train.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Data
@Entity
@Table(name = "roles")
@Component
public class Role {


    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;


}

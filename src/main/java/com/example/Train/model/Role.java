package com.example.Train.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "roles")

public class Role {


    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Role(int i, String user) {
    }

    public Role() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

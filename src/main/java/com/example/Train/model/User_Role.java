package com.example.Train.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "users_roles")
@Component
public class User_Role {
    @Column(name = "user_id")
    Long user_id;
    @Column(name = "role_id")
    int role_id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User_Role() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public User_Role(Long user_id, int role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }
}

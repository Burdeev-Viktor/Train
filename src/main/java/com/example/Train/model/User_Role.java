package com.example.Train.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "users_roles")
@Component
public class User_Role {
    @Column(name = "user_id")
    Long userId;
    @Column(name = "role_id")
    int roleId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public User_Role() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User_Role(Long user_id, int role_id) {
        this.userId = user_id;
        this.roleId = role_id;
    }

    public Long getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }
}

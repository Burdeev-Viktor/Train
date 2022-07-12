package com.example.Train.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@Component
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles() {
        this.roles = roles;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    @Transient
    private String confPassword;



    public Long getWallet() {
        return wallet;
    }

    public void setWallet(Long wallet) {
        this.wallet = wallet;
    }

    @Column(name = "wallet")
    private Long wallet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}

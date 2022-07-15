package com.example.Train.service;

import com.example.Train.Calculation;
import com.example.Train.model.Train;
import com.example.Train.model.User;
import com.example.Train.model.User_Role;
import com.example.Train.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Objects;

@Service
public class UserService {
    private final UserRoleService userRoleService;
    private final TicketService ticketService;
    private final TrainService trainService;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRoleService userRoleService, TicketService ticketService, TrainService trainService, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService) {
        this.userRoleService = userRoleService;
        this.ticketService = ticketService;
        this.trainService = trainService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }


    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean userRolePageMapping(String username) {
        User user = findUserByUsername(username);
        User_Role role = userRoleService.findByUser_Id(user.getId());
        return role.getRoleId() == roleService.getRoleIdByNameRole("ADMIN");
    }

    public boolean userBuyTicket(String username, Long trainId) {
        Train train = trainService.getTrain(trainId);
        User user = findUserByUsername(username);
        if (train.getPrice() <= user.getWallet()) {
            ticketService.saveByTrainUser(train, username);
            train.setSeat_buy(train.getSeat_buy() + 1);
            trainService.saveTrain(train);
            user.setWallet(user.getWallet() - train.getPrice());
            save(user);
            return true;
        }
        return false;
    }

    public void refillBalance(String username, float wallet) {
        User user = findUserByUsername(username);
        user.setWallet(user.getWallet() + Calculation.rounding(wallet));
        save(user);
    }

    public boolean createNewUser(User user, Model model) {
        User userFromdb = findUserByUsername(user.getUsername());
        if (userFromdb != null) {
            model.addAttribute("massageUserIsExist", "Такой пользователь существует!");
            return false;
        }
        if (!Objects.equals(user.getPassword(), user.getConfPassword())) {
            model.addAttribute("massagePasswordsNotMatch", "Пороли не совпадают");
            return false;
        }
        user.setWallet(0.0f);
        add(user);
        user = findUserByUsername(user.getUsername());
        userRoleService.save(new User_Role(user.getId(), roleService.getRoleIdByNameRole("USER")));
        return true;
    }


}

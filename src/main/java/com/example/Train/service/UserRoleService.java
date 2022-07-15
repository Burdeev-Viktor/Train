package com.example.Train.service;

import com.example.Train.model.User_Role;
import com.example.Train.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void save(User_Role user_role) {
        userRoleRepository.save(user_role);
    }

    public User_Role findByUser_Id(Long id) {
        List<User_Role> roles = userRoleRepository.findAll();
        for (User_Role role : roles) {
            if (role.getUserId() == id) {
                return role;
            }
        }
        return null;
    }
}

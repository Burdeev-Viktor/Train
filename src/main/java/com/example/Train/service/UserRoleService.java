package com.example.Train.service;

import com.example.Train.model.User_Role;
import com.example.Train.repository.UserRepository;
import com.example.Train.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;
    public void save(User_Role user_role){
        userRoleRepository.save(user_role);
    }
}

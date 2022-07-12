package com.example.Train.service;

import com.example.Train.model.User_Role;
import com.example.Train.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;
    public void save(User_Role user_role){
        userRoleRepository.save(user_role);
    }
    public User_Role findByUser_Id(Long id){
        List<User_Role> roles = userRoleRepository.findAll();
        for (int i = 0;roles.size()>i;i++){
            if(roles.get(i).getUser_id()==id){
                return roles.get(i);
            }
        }
        return null;
    }
}

package com.example.Train.service;

import com.example.Train.model.Role;
import com.example.Train.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public int getRoleIdByNameRole(String nameRole){
        List<Role> roleList = roleRepository.findAll();
        if(roleList.isEmpty()){
            roleList=createRoles();
        }
        Role role = new Role();
        for (Role value : roleList) {
            if (Objects.equals(value.getName(), nameRole)) {
                role = value;
            }
        }
        return role.getId();
    }
    private List<Role> createRoles(){
        Role admin=new Role();
        admin.setName("ADMIN");
        Role user=new Role();
        user.setName("USER");
        roleRepository.save(admin);
        roleRepository.save(user);
        return roleRepository.findAll();
    }
}

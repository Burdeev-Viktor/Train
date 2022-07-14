package com.example.Train.repository;

import com.example.Train.model.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface UserRoleRepository extends JpaRepository<User_Role, Long> {


}

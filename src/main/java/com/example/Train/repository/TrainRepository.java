package com.example.Train.repository;

import com.example.Train.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TrainRepository extends JpaRepository<Train, Long> {

}

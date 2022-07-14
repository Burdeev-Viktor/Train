package com.example.Train.service;

import com.example.Train.model.Train;
import com.example.Train.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class TrainService {

    private final TrainRepository trainRepository;

    @Autowired
    public TrainService(JpaRepository<Train, Long> trainRepository) {
        this.trainRepository = (TrainRepository) trainRepository;
    }


    public Train getTrain(Long id) {
        return trainRepository.getById(id);
    }

    public List<Train> getAll() {
        return trainRepository.findAll();
    }

    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }

    public void deleteById(Long id) {
        trainRepository.deleteById(id);
    }
}

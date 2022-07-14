package com.example.Train.service;

import com.example.Train.model.Ticket;
import com.example.Train.model.Train;
import com.example.Train.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public void saveByTrainUser(Train train, String username){
        Ticket ticket = new Ticket();
        ticket.setUsername(username);
        ticket.setStart(train.getStart());
        ticket.setEnd(train.getEnd());
        ticket.setPrice(train.getPrice());
        ticket.setTimeStart(train.getTimeStart());
        ticket.setDate(train.getDate());
        ticket.setIdTrain(train.getIdtrain());
        ticketRepository.save(ticket);
    }
    public List<Ticket> findByUsername(String username){
        return ticketRepository.findByUsername(username);
    }
}

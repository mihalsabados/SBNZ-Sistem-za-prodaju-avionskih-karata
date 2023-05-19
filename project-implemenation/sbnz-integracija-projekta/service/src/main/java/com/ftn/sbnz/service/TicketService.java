package com.ftn.sbnz.service;

import com.ftn.sbnz.dto.ticket.PassengerDataDTO;
import com.ftn.sbnz.dto.ticket.TicketDataDTO;
import com.ftn.sbnz.exception.UserNotFoundException;
import com.ftn.sbnz.model.Ticket;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repository.FlightRepository;
import com.ftn.sbnz.repository.TicketRepository;
import com.ftn.sbnz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public boolean createTicket(TicketDataDTO ticketDataDTO) {
        Ticket ticket = new Ticket();
        User passenger = this.userRepository.findByEmail(ticketDataDTO.getPassengerData().getEmailPassenger()).
                orElse(createNewUser(ticketDataDTO.getPassengerData()));
        ticket.setPassenger(passenger);

        User payer = this.userRepository.findByEmail(ticketDataDTO.getPayerEmail()).
                orElseThrow(() -> new UserNotFoundException("User with this email not found!"));

        ticket.setPayer(payer);
        ticket.setId(this.ticketRepository.count() + 1);
        ticketRepository.save(ticket);
        return true;
    }

    private User createNewUser(PassengerDataDTO passengerData) {
        User user = new User(passengerData);
        this.userRepository.save(user);
        return user;
    }

}

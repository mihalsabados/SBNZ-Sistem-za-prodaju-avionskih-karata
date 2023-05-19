package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.FlightDTO;
import com.ftn.sbnz.dto.LoginDTO;
import com.ftn.sbnz.dto.ticket.TicketDataDTO;
import com.ftn.sbnz.dto.ticket.TicketToShowDTO;
import com.ftn.sbnz.model.Ticket;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.service.FlightService;
import com.ftn.sbnz.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketToShowDTO> createTicket(@RequestBody TicketDataDTO ticketDataDTO) {
        TicketToShowDTO ticketToShowDTO = ticketService.createTicket(ticketDataDTO);
        return ResponseEntity.ok(ticketToShowDTO);
    }
}

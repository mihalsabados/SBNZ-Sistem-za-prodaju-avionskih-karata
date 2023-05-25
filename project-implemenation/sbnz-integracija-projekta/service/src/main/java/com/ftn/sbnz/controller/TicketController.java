package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.FlightDTO;
import com.ftn.sbnz.dto.ticket.TicketDataDTO;
import com.ftn.sbnz.dto.ticket.TicketToShowDTO;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.PriceTemplate;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.service.FlightService;
import com.ftn.sbnz.service.TicketService;
import com.ftn.sbnz.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private final TicketService ticketService;
    private final FlightService flightService;
    private final UserService userService;

    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody TicketDataDTO ticketDataDTO) {
        TicketToShowDTO ticketToShowDTO = ticketService.createTicket(ticketDataDTO);
        if(!ticketToShowDTO.isFlightFound())
            return ResponseEntity.ok(ticketToShowDTO);
        if(ticketToShowDTO.getAlternativeFlightId() != ticketDataDTO.getFlightId()){
            Flight flight = flightService.getFlightById(ticketToShowDTO.getAlternativeFlightId());
            FlightDTO suggestedFlightDTO = new FlightDTO(flight);
            return ResponseEntity.ok(suggestedFlightDTO);
        }
        return ResponseEntity.ok(ticketToShowDTO);
    }

    @PostMapping("/accept-suggested-flight")
    public ResponseEntity<?> acceptSuggestedFlight(@RequestBody TicketDataDTO ticketDataDTO) {
        TicketToShowDTO ticketToShowDTO = ticketService.acceptSuggestedFlight(ticketDataDTO);
        return ResponseEntity.ok(ticketToShowDTO);
    }

    @GetMapping("/get-price-template")
    public ResponseEntity<?> getPriceTemplate() {
        List<PriceTemplate> priceTemplate = ticketService.getPriceTemplate();
        return ResponseEntity.ok(priceTemplate);
    }

    @PostMapping("/set-price-template")
    public ResponseEntity<?> setPriceTemplate(@RequestBody List<PriceTemplate> priceTemplates) {
        ticketService.setPriceTemplate(priceTemplates);
        return ResponseEntity.ok().build();
    }
}

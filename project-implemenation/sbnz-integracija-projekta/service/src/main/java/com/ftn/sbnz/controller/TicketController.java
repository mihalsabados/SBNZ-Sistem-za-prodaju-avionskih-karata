package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.FlightDTO;
import com.ftn.sbnz.dto.ReportDTO;
import com.ftn.sbnz.dto.SortTicketsDTO;
import com.ftn.sbnz.dto.TicketDTO;
import com.ftn.sbnz.dto.ticket.TicketDataDTO;
import com.ftn.sbnz.dto.ticket.TicketToShowDTO;
import com.ftn.sbnz.exception.UserIsBlockedException;
import com.ftn.sbnz.model.TicketsReportTemplate;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.PriceTemplate;
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

    @GetMapping("/")
    public ResponseEntity<?> getAllTickets(){
        ReportDTO reportDTO = ticketService.getAllTickets();
        return ResponseEntity.ok(reportDTO);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getTicketsForUser(@PathVariable String email){
        List<TicketDTO> tickets = ticketService.getTicketsForUser(email);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody TicketDataDTO ticketDataDTO) {
        try {
            TicketToShowDTO ticketToShowDTO = ticketService.createTicket(ticketDataDTO);
            if(!ticketToShowDTO.isFlightFound())
                return ResponseEntity.ok(ticketToShowDTO);
            if(ticketToShowDTO.getAlternativeFlightId() != ticketDataDTO.getFlightId()){
                Flight flight = flightService.getFlightById(ticketToShowDTO.getAlternativeFlightId());
                FlightDTO suggestedFlightDTO = new FlightDTO(flight);
                return ResponseEntity.ok(suggestedFlightDTO);
            }
            return ResponseEntity.ok(ticketToShowDTO);
        } catch (UserIsBlockedException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/accept-suggested-flight")
    public ResponseEntity<?> acceptSuggestedFlight(@RequestBody TicketDataDTO ticketDataDTO) {
        TicketToShowDTO ticketToShowDTO = ticketService.createTicket(ticketDataDTO);
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

    @PostMapping("/tickets-report")
    public ResponseEntity<?> getTicketsReport(@RequestBody TicketsReportTemplate filterTicketsTemplate) {
        ReportDTO reportDTO = ticketService.getTicketsReport(filterTicketsTemplate);
        return ResponseEntity.ok(reportDTO);
    }


    @PostMapping("/sort-tickets")
    public ResponseEntity<?> sortTickets(@RequestBody SortTicketsDTO sortTicketsDTO) {
        List<TicketDTO> sortedTickets = ticketService.sortTickets(sortTicketsDTO);
        return ResponseEntity.ok(sortedTickets);
    }
}

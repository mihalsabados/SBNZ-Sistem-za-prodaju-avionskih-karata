package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.FlightDTO;
import com.ftn.sbnz.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flight")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/")
    public ResponseEntity<?> getFlights() {
        List<FlightDTO> flights = flightService.getAllFlights();

        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
}

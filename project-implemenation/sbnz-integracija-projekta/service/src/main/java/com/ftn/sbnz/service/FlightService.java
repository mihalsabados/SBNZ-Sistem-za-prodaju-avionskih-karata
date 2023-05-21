package com.ftn.sbnz.service;

import com.ftn.sbnz.dto.FlightDTO;
import com.ftn.sbnz.exception.FlightNotFoundException;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;


    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll()
                .stream()
                .map(FlightDTO::new)
                .collect(Collectors.toList());
    }

    public Flight getFlightById(Long flightId) {
        return this.flightRepository.findById(flightId).
                orElseThrow(() -> new FlightNotFoundException("Flight with this id not found!"));
    }
}

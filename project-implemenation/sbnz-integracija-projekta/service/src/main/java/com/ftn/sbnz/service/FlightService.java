package com.ftn.sbnz.service;

import com.ftn.sbnz.dto.FlightDTO;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
}

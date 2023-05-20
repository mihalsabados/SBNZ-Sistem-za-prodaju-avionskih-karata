package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private Long id;
    private String destination;
    private int distance;
    private double price;
    private String departure;
    private int soldTickets;
    private int numberOfSeats;
    private boolean popular;

    public FlightDTO(Flight flight){
        this.id = flight.getId();
        this.destination = flight.getDestination();
        this.distance = flight.getDistance();
        this.price = flight.getPrice();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.departure = flight.getDeparture().format(formatter);
        this.soldTickets = flight.getSoldTickets().size();
        this.numberOfSeats = flight.getNumberOfSeats();
        this.popular = flight.isPopular();
    }
}

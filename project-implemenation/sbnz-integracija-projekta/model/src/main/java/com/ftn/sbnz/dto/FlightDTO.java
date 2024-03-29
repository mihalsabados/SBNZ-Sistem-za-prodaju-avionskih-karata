package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
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
    private String flightNumber;

    public FlightDTO(Flight flight){
        this.id = flight.getId();
        this.destination = flight.getDestination();
        this.distance = flight.getDistance();
        this.price = flight.getPrice();
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        this.departure = ft.format(flight.getDeparture());
        this.soldTickets = flight.getSoldTickets().size();
        this.numberOfSeats = flight.getNumberOfSeats();
        this.popular = flight.isPopular();
        this.flightNumber = flight.getFlightNumber();
    }
}

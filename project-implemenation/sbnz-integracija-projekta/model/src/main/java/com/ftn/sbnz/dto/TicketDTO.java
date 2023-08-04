package com.ftn.sbnz.dto;

import com.ftn.sbnz.enums.TicketType;
import com.ftn.sbnz.model.Discount;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.Ticket;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketDTO {
    private String flightNo;
    private String destination;
    private String departure;
    private String passengerEmail;
    private String payerEmail;
    private List<Discount> discounts;
    private double finalPrice;
    private double basePrice;
    private TicketType ticketType;
    private String timestamp;

    public TicketDTO(Ticket ticket, Flight flight){
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        this.flightNo = flight.getFlightNumber();
        this.destination = flight.getDestination();
        this.departure = ft.format(flight.getDeparture());
        this.passengerEmail = ticket.getPassenger().getEmail();
        this.payerEmail = ticket.getPayer().getEmail();
        this.discounts = ticket.getDiscounts();
        this.finalPrice = ticket.getFinalPrice();
        this.basePrice = ticket.getBasePrice();
        this.ticketType = ticket.getTicketType();
        this.timestamp = ft.format(ticket.getTimestamp());
    }

    public Date getDepartureInDate(){
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            return ft.parse(this.departure);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date getTimestampInDate(){
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            return ft.parse(this.timestamp);
        } catch (ParseException e) {
            return null;
        }
    }

}

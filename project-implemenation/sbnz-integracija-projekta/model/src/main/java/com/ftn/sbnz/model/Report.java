package com.ftn.sbnz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Report {
    private int numberOfTickets;
    private int numberOfBusinessTickets;
    private int numberOfEconomicTickets;
    private int numberOfFlights;
    private double totalAmount;
    private double averageOccupancy;
    private double averagePrice;
    private List<Ticket> tickets = new ArrayList<>();;

}

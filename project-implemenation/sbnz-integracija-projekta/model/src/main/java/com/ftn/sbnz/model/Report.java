package com.ftn.sbnz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Report {
    private int numberOfTickets;
    private double totalAmount;
    private double averageOccupancy;
    private double averagePrice;
    private List<Ticket> tickets;

    public Report(){
        this.tickets = new ArrayList<>();
    }

}

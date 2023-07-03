package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.Report;
import com.ftn.sbnz.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ReportDTO {
    private int numberOfTickets;
    private int numberOfFlights;
    private int numberOfBusinessTickets;
    private int numberOfEconomicTickets;
    private double totalAmount;
    private double averageOccupancy;
    private double averagePrice;
    private List<TicketDTO> tickets;

    public ReportDTO(){
        this.tickets = new ArrayList<>();
    }

    public ReportDTO(Report report){
        this.numberOfTickets = report.getNumberOfTickets();
        this.numberOfFlights = report.getNumberOfFlights();
        this.numberOfBusinessTickets = report.getNumberOfBusinessTickets();
        this.numberOfEconomicTickets = report.getNumberOfEconomicTickets();
        this.totalAmount = report.getTotalAmount();
        this.averageOccupancy = report.getAverageOccupancy();
        this.averagePrice = report.getAveragePrice();

    }
}

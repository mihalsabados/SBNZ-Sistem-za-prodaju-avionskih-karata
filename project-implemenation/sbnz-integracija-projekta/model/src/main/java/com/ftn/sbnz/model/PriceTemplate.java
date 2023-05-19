package com.ftn.sbnz.model;


import com.ftn.sbnz.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceTemplate {
    private TicketType ticketType;
    private int minDistance;
    private int maxDistance;
    private double price;
}

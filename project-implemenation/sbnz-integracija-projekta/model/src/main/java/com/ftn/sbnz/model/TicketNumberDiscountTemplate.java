package com.ftn.sbnz.model;

import com.ftn.sbnz.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketNumberDiscountTemplate {
    private int minTicketNumber;
    private int maxTicketNumber;
    private TicketType ticketType;
    private String discountName;
}

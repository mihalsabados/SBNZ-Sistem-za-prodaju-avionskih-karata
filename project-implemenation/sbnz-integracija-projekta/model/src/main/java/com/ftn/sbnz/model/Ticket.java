package com.ftn.sbnz.model;

import com.ftn.sbnz.enums.TicketType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Ticket {
    @Id
    private Long id;
    private User passenger;
    private User payer;
    @DBRef
    private List<Discount> discounts;
    private double finalPrice;
    private double basePrice;
    private TicketType ticketType;
}

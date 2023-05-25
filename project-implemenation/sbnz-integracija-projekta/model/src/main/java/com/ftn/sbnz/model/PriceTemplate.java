package com.ftn.sbnz.model;


import com.ftn.sbnz.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("priceTemplates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceTemplate {
    @Id
    private Long id;
    private TicketType ticketType;
    private int minDistance;
    private int maxDistance;
    private double price;
}

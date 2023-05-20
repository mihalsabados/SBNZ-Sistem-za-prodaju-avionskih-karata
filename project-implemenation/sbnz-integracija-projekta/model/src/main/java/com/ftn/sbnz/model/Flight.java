package com.ftn.sbnz.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Flight {
    @Id
    private Long id;
    private String destination;
    private int distance;
    private double price;
    private LocalDateTime departure;
    @DBRef
    private List<Ticket> soldTickets;
    private int numberOfSeats;
    private boolean popular;

}

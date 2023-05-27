package com.ftn.sbnz.model;

import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Role(Role.Type.EVENT)
@Timestamp("departure")
@Expires("2h30m")
public class Flight {
    @Id
    private Long id;
    private String destination;
    private int distance;
    private double price;
    private Date departure;
    @DBRef
    private List<Ticket> soldTickets;
    private int numberOfSeats;
    private boolean popular;
    private String flightNumber;

}

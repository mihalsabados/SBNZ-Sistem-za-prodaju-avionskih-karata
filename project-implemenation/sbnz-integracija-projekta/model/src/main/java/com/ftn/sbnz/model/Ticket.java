package com.ftn.sbnz.model;

import com.ftn.sbnz.enums.TicketType;
import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document("tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("2h30m")
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
    private Date timestamp;
}

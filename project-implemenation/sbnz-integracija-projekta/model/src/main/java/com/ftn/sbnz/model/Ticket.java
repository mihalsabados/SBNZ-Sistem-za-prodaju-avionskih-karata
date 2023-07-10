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
import java.util.Objects;

@Document("tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("500d")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

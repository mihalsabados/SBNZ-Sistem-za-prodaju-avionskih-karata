package com.ftn.sbnz.model;

import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("redundantPaymentEvents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("100d")
public class RedundantPaymentEvent {
    @Id
    private Long id;
    private Long flightId;
    private Date timestamp;
    private String payerEmail;
    private Long ticket1Id;
    private Long ticket2Id;

    public RedundantPaymentEvent(Long flightId, String payerEmail, Long ticket1Id, Long ticket2Id){
        this.flightId = flightId;
        this.payerEmail = payerEmail;
        this.ticket1Id = ticket1Id;
        this.ticket2Id = ticket2Id;
        this.timestamp = new Date();
    }
}

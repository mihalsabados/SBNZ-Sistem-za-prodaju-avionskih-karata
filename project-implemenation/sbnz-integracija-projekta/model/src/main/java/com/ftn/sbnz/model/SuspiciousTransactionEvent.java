package com.ftn.sbnz.model;

import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("suspiciousTransactionEvents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("500d")
public class SuspiciousTransactionEvent {
    @Id
    private Long id;
    private Long flightId;
    private String payerEmail;
    private Date timestamp;
    private Long ticket1Id;
    private Long ticket2Id;

    public SuspiciousTransactionEvent(Long flightId, String payerEmail, Long ticket1Id, Long ticket2Id){
        this.flightId = flightId;
        this.payerEmail = payerEmail;
        this.timestamp = new Date();
        this.ticket1Id = ticket1Id;
        this.ticket2Id = ticket2Id;
    }
}
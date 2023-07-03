package com.ftn.sbnz.model;

import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("lastMinuteEvents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("100d")
public class LastMinuteEvent {
    @Id
    private Long id;
    private Long flightId;
    private Date timestamp;

    public LastMinuteEvent(Long flightId){
        this.flightId = flightId;
        this.timestamp = new Date();
    }
}

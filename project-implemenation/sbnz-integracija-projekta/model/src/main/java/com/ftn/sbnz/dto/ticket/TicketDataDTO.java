package com.ftn.sbnz.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDataDTO {
    private PassengerDataDTO passengerData;
    private String payerEmail;
    private Long flightId;
    private String cardType;
}

package com.ftn.sbnz.dto.ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDataDTO {
    private String emailPassenger;
    private String firstNamePassenger;
    private String lastNamePassenger;
    private String addressPassenger;
    private String phoneNumberPassenger;
    private String placePassenger;
}

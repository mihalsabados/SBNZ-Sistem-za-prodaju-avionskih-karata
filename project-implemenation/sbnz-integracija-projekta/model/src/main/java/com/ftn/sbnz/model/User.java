package com.ftn.sbnz.model;
import com.ftn.sbnz.dto.ticket.PassengerDataDTO;
import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.UserType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String place;
    private LoyaltyType loyaltyStatus;
    private UserType userType;

    public User(PassengerDataDTO passengerData) {
        this.email = passengerData.getEmailPassenger();
        this.password = "12345678";
        this.firstName = passengerData.getFirstNamePassenger();
        this.lastName = passengerData.getLastNamePassenger();
        this.address = passengerData.getAddressPassenger();
        this.phoneNumber = passengerData.getPhoneNumberPassenger();
        this.place = passengerData.getPlacePassenger();
        this.loyaltyStatus = LoyaltyType.REGULAR;
    }
}

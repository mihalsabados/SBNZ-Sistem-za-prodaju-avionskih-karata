package com.ftn.sbnz.model;
import com.ftn.sbnz.dto.ticket.PassengerDataDTO;
import com.ftn.sbnz.enums.LoyaltyType;
import com.ftn.sbnz.enums.UserType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private boolean blocked;

    public User(PassengerDataDTO passengerData) {
        this.email = passengerData.getEmailPassenger();
        this.password = "12345678";
        this.firstName = passengerData.getFirstNamePassenger();
        this.lastName = passengerData.getLastNamePassenger();
        this.address = passengerData.getAddressPassenger();
        this.phoneNumber = passengerData.getPhoneNumberPassenger();
        this.place = passengerData.getPlacePassenger();
        this.loyaltyStatus = LoyaltyType.REGULAR;
        this.userType = UserType.PASSENGER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}

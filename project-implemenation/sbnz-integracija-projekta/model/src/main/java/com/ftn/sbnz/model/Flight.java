package com.ftn.sbnz.model;

import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Document("flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Role(Role.Type.EVENT)
@Timestamp("departure")
@Expires("100d")
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

    public boolean isDepartureBetweenDates(String dateFromStr, String dateToStr) throws ParseException {
        Date dateFrom, dateTo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfPattern = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        if(dateFromStr.equals("null"))
            dateFrom = sdf.parse("01-01-2000");
        else
            dateFrom = sdfPattern.parse(dateFromStr);
        if(dateToStr.equals("null"))
            dateTo = sdf.parse("01-01-2040");
        else
            dateTo = sdfPattern.parse(dateToStr);

        return dateFrom.compareTo(departure) * departure.compareTo(dateTo) > 0;
    }

    public boolean isDestinationNull(String destination){
        return destination.equals("null");
    }

}

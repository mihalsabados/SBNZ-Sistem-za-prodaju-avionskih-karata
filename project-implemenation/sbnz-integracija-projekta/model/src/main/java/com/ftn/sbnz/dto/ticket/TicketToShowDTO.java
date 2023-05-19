package com.ftn.sbnz.dto.ticket;
import com.ftn.sbnz.model.Discount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketToShowDTO {
    private List<Discount> discounts;
    private double finalPrice;
}

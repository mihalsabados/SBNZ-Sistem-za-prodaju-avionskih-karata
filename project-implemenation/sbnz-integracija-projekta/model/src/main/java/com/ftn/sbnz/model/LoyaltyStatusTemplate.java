package com.ftn.sbnz.model;

import com.ftn.sbnz.enums.LoyaltyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoyaltyStatusTemplate {
    private double minPrice;
    private double maxPrice;
    private LoyaltyType status;
    private String discountName;
}

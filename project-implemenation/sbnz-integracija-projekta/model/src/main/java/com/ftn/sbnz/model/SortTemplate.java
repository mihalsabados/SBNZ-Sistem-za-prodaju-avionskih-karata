package com.ftn.sbnz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SortTemplate {
    private String sortParameter;
    private boolean ascOrder;
    private int sortSalience;
    private int orderSalience;
}

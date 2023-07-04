package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.TicketsReportTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SortDTO {
    private TicketsReportTemplate ticketsReportTemplate;
    private String sortBy;
    private boolean ascOrder;
}

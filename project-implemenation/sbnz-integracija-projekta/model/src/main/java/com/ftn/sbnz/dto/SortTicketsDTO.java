package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.SortTemplate;
import com.ftn.sbnz.model.TicketsReportTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SortTicketsDTO {
    private TicketsReportTemplate ticketsReportTemplate;
    private SortTemplate sortTemplate;
}

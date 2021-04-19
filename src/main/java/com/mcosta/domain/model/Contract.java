package com.mcosta.domain.model;

import java.time.LocalDate;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode @ToString
public class Contract {
    
    @Getter @Setter
    private LocalDate effectiveStartDate;

    @Getter @Setter
    private Integer durationInMonths;

    @Getter @Setter
    private Client client;

    @Getter
    private List<Equipment> equipments;

    public Contract(LocalDate effectiveStartDate, Integer durationInMonths, Client client) {
        this.effectiveStartDate = effectiveStartDate;
        this.durationInMonths = durationInMonths;
        this.client = client;
    }

}

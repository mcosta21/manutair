package com.mcosta.domain.model;

import java.time.LocalDate;
import java.util.List;

import com.mcosta.util.DateFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
public class Contract {

    @Getter @Setter
    private Long idContract;

    @Getter @Setter
    private LocalDate effectiveStartDate;

    @Getter @Setter
    private Integer durationInMonths;

    @Getter @Setter
    private Client client;

    public Contract(LocalDate effectiveStartDate, Integer durationInMonths, Client client) {
        this.effectiveStartDate = effectiveStartDate;
        this.durationInMonths = durationInMonths;
        this.client = client;
    }

    @Override
    public String toString() {
        return "Contrato " + idContract + " - " + "Data de início de vigência: " + effectiveStartDate;
    }

    public String getClientName(){
        Object object = this.client;
        if(object instanceof Physical) {
            Physical p = (Physical) object;
            return p.getName();
        }
        if(object instanceof Legal) {
            Legal l = (Legal) object;
            return l.getCompanyName();
        }
        return "";
    }

    public String getEffectiveStartDateFormatted(){
        return DateFormatter.LocalDateFormatted(this.effectiveStartDate);
    }

    public String getDurationInMonthsText(){
        if(durationInMonths == 1) {
            return durationInMonths + " mês";
        }
        return durationInMonths + " meses";
    }
}

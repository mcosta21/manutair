package com.mcosta.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.mcosta.domain.enumeration.StatusServiceOrderEnum;

import com.mcosta.util.DateFormatter;
import lombok.Getter;
import lombok.Setter;

public class ServiceOrder {

    @Getter @Setter
    private Long idServiceOrder;

    @Getter @Setter
    private String equipmentLocationAddress;

    @Getter @Setter
    private String problemDescription;

    @Getter @Setter
    private String solutionDescription;

    @Getter
    private LocalDateTime creationDate = LocalDateTime.now();;

    @Getter @Setter
    private LocalDateTime serviceDate;

    @Getter @Setter
    private LocalDate finishDate;

    @Getter @Setter
    private Equipment equipment;

    @Getter @Setter
    private User user;

    @Getter @Setter
    private StatusServiceOrderEnum statusServiceOrderEnum = StatusServiceOrderEnum.OPENED;

    public ServiceOrder(String equipmentLocationAddress, String problemDescription, Equipment equipment) {
        this.equipmentLocationAddress = equipmentLocationAddress;
        this.problemDescription = problemDescription;
        this.equipment = equipment;
    }

    public String getDescriptionEquipment(){
        return this.equipment.getDescription();
    }

    public Long getIdContract(){
        return this.equipment.getContract().getIdContract();
    }

    public String getClientName(){
        Object object = this.equipment.getContract().getClient();
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

    public String getCreationDateFormatted(){
        return DateFormatter.LocalDateTimeFormatted(this.creationDate);
    }

    public String getStatusServiceOrder(){
        return this.statusServiceOrderEnum.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceOrder)) return false;
        ServiceOrder that = (ServiceOrder) o;
        return Objects.equals(idServiceOrder, that.idServiceOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idServiceOrder);
    }
}

package com.mcosta.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class Equipment {

    @Getter @Setter
    private Long idEquipment;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String brand;

    @Getter @Setter
    private String model;

    @Getter @Setter
    private String serialNumber;

    @Getter @Setter
    private Boolean isDeleted = false;

    @Getter @Setter
    private Contract contract;

    public Equipment(String description, String brand, String model, String serialNumber, Contract contract) {
        this.description = description;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.contract = contract;
    }

    @Override
    public String toString() {
        return description + " " + brand + " " + model + " " + serialNumber;
    }

    public Long getIdContract(){
        return this.contract.getIdContract();
    }

    public String getClient(){
        return this.contract.getClientName();
    }
}

package com.mcosta.domain.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}

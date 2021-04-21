package com.mcosta.domain.validator;

import com.mcosta.domain.model.Equipment;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;

public class EquipmentValidator {

    private String message;

    public boolean isValid(Equipment equipment) {

        if(!isValidFast(equipment)){
            return false;
        }

        if(equipment.getContract() == null){
            message = "Contrato não informado.";
            return false;
        }

        return true;
    }

    public boolean isValidFast(Equipment equipment) {

        if(equipment.getDescription() == null || equipment.getDescription().isEmpty()){
            message = "Descrição não informada.";
            return false;
        }

        if(equipment.getBrand() == null || equipment.getBrand().isEmpty()){
            message = "Marca não informada.";
            return false;
        }

        if(equipment.getModel() == null || equipment.getModel().isEmpty()){
            message = "Modelo não informada.";
            return false;
        }

        if(equipment.getSerialNumber() == null || equipment.getSerialNumber().isEmpty()){
            message = "Número de série não informado.";
            return false;
        }

        return true;
    }

    public String getMessage() {
        return message;
    }
}

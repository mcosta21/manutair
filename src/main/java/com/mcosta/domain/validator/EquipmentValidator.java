package com.mcosta.domain.validator;

import com.mcosta.domain.model.Equipment;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;

public class EquipmentValidator {

    private String message;

    public boolean isValid(Equipment physical) {

        /*
        if (client.getName() == null || client.getName().isEmpty()) {
            message = "Nome não informado.";
            return false;
        }*/

        return true;
    }


    public String getMessage() {
        return message;
    }
}

package com.mcosta.domain.validator;

import com.mcosta.domain.model.Equipment;
import com.mcosta.domain.model.ServiceOrder;

public class ServiceOrderValidator {

    private String message;

    public boolean isValid(ServiceOrder physical) {

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

package com.mcosta.domain.validator;

import com.mcosta.domain.model.Contract;

public class ContractValidator {

    private String message;

    public boolean isValid(Contract contract) {

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

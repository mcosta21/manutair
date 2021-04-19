package com.mcosta.domain.validator;

import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;

public class ClientValidator {

    private String message;

    public boolean isValid(Physical physical) {

        /*
        if (client.getName() == null || client.getName().isEmpty()) {
            message = "Nome não informado.";
            return false;
        }*/

        return true;
    }

    public boolean isValid(Legal legal) {

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

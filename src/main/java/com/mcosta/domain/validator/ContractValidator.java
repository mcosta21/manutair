package com.mcosta.domain.validator;

import com.mcosta.domain.model.Contract;

public class ContractValidator {

    private String message;

    public boolean isValid(Contract contract) {

        if (contract.getEffectiveStartDate() == null) {
            message = "Data de início da vigência não informado.";
            return false;
        }

        if (contract.getDurationInMonths() == null) {
            message = "Duração em meses não informado.";
            return false;
        }

        if (contract.getClient() == null) {
            message = "Cliente não informado.";
            return false;
        }

        return true;
    }

    public String getMessage() {
        return message;
    }
}

package com.mcosta.domain.validator;

import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;
import com.mcosta.util.ValidatorCNPJ;
import com.mcosta.util.ValidatorCpf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator {

    private String message;
    private ValidatorCpf validatorCpf = new ValidatorCpf();
    private ValidatorCNPJ validatorCNPJ = new ValidatorCNPJ();

    public boolean isValid(Physical client) {

        if (client.getName() == null || client.getName().isEmpty()) {
            message = "Nome não informado.";
            return false;
        }

        if (client.getCpf() == null || client.getCpf().isEmpty()) {
            message = "CPF não informado.";
            return false;
        }

        if (validatorCpf.isNotValido(client.getCpf())) {
            message = "CPF informado não é válido.";
            return false;
        }

        if (client.getPhone() == null || client.getPhone().isEmpty()) {
            message = "Telefone não informado.";
            return false;
        }

        String phone_regex = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})";
        Pattern patternPhone = Pattern.compile(phone_regex);
        Matcher matcherTelephone = patternPhone.matcher(client.getPhone());
        if(!client.getPhone().isEmpty() && !matcherTelephone.matches()) {
            message = "Telefone informado é inválido.";
            return false;
        }

        if (client.getAddress() == null || client.getAddress().isEmpty()) {
            message = "Endereço não informado.";
            return false;
        }

        return true;
    }

    public boolean isValid(Legal client) {

        if (client.getCompanyName() == null || client.getCompanyName().isEmpty()) {
            message = "Razão Social não informada.";
            return false;
        }

        if (client.getCnpj() == null || client.getCnpj().isEmpty()) {
            message = "CNPJ não informado.";
            return false;
        }

        if (validatorCNPJ.isNotValido(client.getCnpj())) {
            message = "CNPJ informado não é válido.";
            return false;
        }

        if (client.getPhone() == null || client.getPhone().isEmpty()) {
            message = "Telefone não informado.";
            return false;
        }

        String phone_regex = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})";
        Pattern patternPhone = Pattern.compile(phone_regex);
        Matcher matcherTelephone = patternPhone.matcher(client.getPhone());
        if(!client.getPhone().isEmpty() && !matcherTelephone.matches()) {
            message = "Telefone informado é inválido.";
            return false;
        }

        if (client.getAddress() == null || client.getAddress().isEmpty()) {
            message = "Endereço não informado.";
            return false;
        }

        return true;
    }

    public String getMessage() {
        return message;
    }
}

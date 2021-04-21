package com.mcosta.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Physical extends Client {
    
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String cpf;

    public Physical(String name, String cpf, String phone, String address) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String toString() {
        return cpf + " - " + name;
    }
    
}

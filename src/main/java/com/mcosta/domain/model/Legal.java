package com.mcosta.domain.model;

import lombok.Getter;
import lombok.Setter;

public class Legal extends Client {
    
    @Getter @Setter
    private String companyName;

    @Getter @Setter
    private String cnpj;

    public Legal(String companyName, String cnpj, String phone, String address) {
        this.companyName = companyName;
        this.cnpj = cnpj;
        this.phone = phone;
        this.address = address;
    }
    
    @Override
    public String toString() {
        return cnpj + " - " + companyName;
    }
}


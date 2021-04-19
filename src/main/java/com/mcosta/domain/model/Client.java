package com.mcosta.domain.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Client {

    @Getter @Setter
    protected String phone;

    @Getter @Setter
    protected String address;

    @Getter
    protected List<Contract> contracts;
    
}

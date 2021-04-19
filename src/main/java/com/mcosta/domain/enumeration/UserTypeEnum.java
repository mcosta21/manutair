package com.mcosta.domain.enumeration;

public enum UserTypeEnum {
    ATTENDANT ("Atendente"),
    SUPERVISOR ("Supervisor"),
    TECHNICIAN ("Técnico"),
    ADMIN ("Administrador");

    private final String value;

    UserTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return getValue();
    }
}

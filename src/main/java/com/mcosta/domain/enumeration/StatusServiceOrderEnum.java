package com.mcosta.domain.enumeration;

public enum StatusServiceOrderEnum {
    OPENED ("Aberta"),
    IN_PROGRESS ("Em andamento"),
    CONCLUDED ("Concluída");

    private final String value;

    StatusServiceOrderEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

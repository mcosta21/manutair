package com.mcosta.domain.model;

import java.util.List;

import com.mcosta.domain.enumeration.UserTypeEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode @ToString
public class User {

    @Getter @Setter
    private Long idUser;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private boolean isTechnician;

    @Getter @Setter
    private UserTypeEnum userType;
    
    @Getter
    private List<ServiceOrder> serviceOrders;
    
    public User(String username, String password, String name, UserTypeEnum userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }


}

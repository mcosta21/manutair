package com.mcosta.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcosta.domain.enumeration.UserTypeEnum;

import com.mcosta.domain.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;

public abstract class AccessProvider {
    
    @Getter @Setter
    private static User user;

    public static List<Page> getPagesByUserType(UserTypeEnum userType) {
        switch(userType) {
            case ATTENDANT:
                System.out.println("atendente");
                return pagesForAttendant();
            case SUPERVISOR:
                return pagesForSupervisor();
            case TECHNICIAN:
                return pagesForTechnician();
            case ADMIN:
                return pagesForAdmin();
        }

        return null;
    }

    private static List<Page> pagesForAttendant(){
        List<Page> pages = new ArrayList<Page>();
        pages.add(new Page("client", "Clientes"));
        pages.add(new Page("contract", "Contratos"));
        pages.add(new Page("equipment", "Equipamentos"));
        pages.add(new Page("service-order", "Ordem de Serviço"));
        return pages;
    }

    private static List<Page> pagesForSupervisor(){
        List<Page> pages = new ArrayList<Page>();
        pages.add(new Page("service-order", "Ordem de Serviço"));
        pages.add(new Page("user", "Usuários"));
        return pages;
    }

    private static List<Page> pagesForTechnician(){
        List<Page> pages = new ArrayList<Page>();
        pages.add(new Page("service-order", "Ordem de Serviço"));
        return pages;
    }

    private static List<Page> pagesForAdmin(){
        Set<Page> pages = new HashSet<Page>();
        pages.addAll(pagesForAttendant());
        pages.addAll(pagesForSupervisor());
        pages.addAll(pagesForTechnician());
        return new ArrayList<>(pages);
    }

    public static class Page{
        @Getter @Setter
        private String uri;
        @Getter @Setter
        private String label;

        Page(String uri, String label) {
            this.uri = uri;
            this.label = label;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((uri == null) ? 0 : uri.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Page other = (Page) obj;
            if (uri == null) {
                if (other.uri != null)
                    return false;
            } else if (!uri.equals(other.uri))
                return false;
            return true;
        }
        
    }
}

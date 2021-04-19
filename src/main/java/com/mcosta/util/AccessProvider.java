package com.mcosta.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcosta.domain.enumeration.UserTypeEnum;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;

public abstract class AccessProvider {
    
    @Getter @Setter
    private static String username;

    @Getter @Setter
    private static String userType;

    public static List<Page> getPagesByUserType(UserTypeEnum userType) {
        System.out.println(userType.name());
        switch(userType) {
            case ATTENDANT:
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
        return pages;
    }

    private static List<Page> pagesForSupervisor(){
        List<Page> pages = new ArrayList<Page>();
        pages.add(new Page("supervisor", "Supervisor"));
        return pages;
    }

    private static List<Page> pagesForTechnician(){
        List<Page> pages = new ArrayList<Page>();
        pages.add(new Page("technician", "Técnico"));
        pages.add(new Page("view-service-orders", "Visualizar Ordens"));
        return pages;
    }

    private static List<Page> pagesForAdmin(){
        Set<Page> pages = new HashSet<Page>();
        pages.add(new Page("user", "Usuários"));
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

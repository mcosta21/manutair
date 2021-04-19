package com.mcosta.controller;

import java.io.IOException;

import com.mcosta.util.AccessProvider;
import com.mcosta.util.ManagerWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AccessProviderController {
    
    @FXML
    private void goHome(ActionEvent event) throws IOException {
        ManagerWindow.openWindow("main", "ManutAir");
        ManagerWindow.closeWindow(event);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        AccessProvider.setUsername(null);
        AccessProvider.setUserType(null);
        ManagerWindow.openWindow("login", "Login");
        ManagerWindow.closeWindow(event);
    }

}

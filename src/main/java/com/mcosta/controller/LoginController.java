package com.mcosta.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Physical;
import com.mcosta.util.ManagerWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class LoginController implements Initializable {

    private ClientDao clientDao = new ClientDao();

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUserType;
    
    @FXML
    private Label lblMostrar;
    
    @FXML
    private void OnClickBackToPrimary(ActionEvent event) throws IOException {
        ManagerWindow.openWindow("primary");
        ManagerWindow.closeWindow(event);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO Auto-generated method stub
        lblMostrar.setText("Login");

    }
    
}

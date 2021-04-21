package com.mcosta.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.dao.UserDao;
import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Physical;
import com.mcosta.domain.model.User;
import com.mcosta.domain.validator.UserValidator;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.ManagerWindow;

import com.mcosta.util.MessageAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    private UserDao userDao = new UserDao();
    private UserValidator userValidator = new UserValidator();

    @FXML
    private TextField inputLogin;

    @FXML
    private PasswordField inputPassword;

    @FXML
    void onClickLogin(ActionEvent event) throws IOException {
        String username = inputLogin.getText();
        String password = inputPassword.getText();

        if(!userValidator.isValidToLogin(username, password)){
            new MessageAlert("Erro", userValidator.getMessage(), Alert.AlertType.INFORMATION).sendMessageAlert();
            return;
        }

        User user = userDao.getUserByUsernameAndPassword(username, password);
        if(user == null) {
            new MessageAlert("Erro", "Usuário ou senha incorreto.", Alert.AlertType.INFORMATION).sendMessageAlert();
            return;
        }

        AccessProvider.setUser(user);
        ManagerWindow.openWindow("main", "ManutAir");
        ManagerWindow.closeWindow(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}

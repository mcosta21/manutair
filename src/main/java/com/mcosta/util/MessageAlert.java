package com.mcosta.util;

import javafx.scene.control.Alert;

public class MessageAlert {

    private final Alert alert = new Alert(Alert.AlertType.ERROR);

    public MessageAlert(String title, String message){
        this.alert.setTitle(title);
        this.alert.setHeaderText(null);
        this.alert.setContentText(message);
    }

    public MessageAlert(String title, String message, Alert.AlertType type){
        this.alert.setTitle(title);
        this.alert.setHeaderText(null);
        this.alert.setContentText(message);
        this.alert.setAlertType(type);
    }

    public void sendMessageAlert(){
        this.alert.showAndWait();
    }

    public void setTypeMessageAlert(Alert.AlertType type) {
        this.alert.setAlertType(type);
    }
}

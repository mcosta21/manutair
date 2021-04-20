package com.mcosta.util;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class ManagerWindow {
    
    public static void openWindow(String page) throws IOException {
        openWindow(page, "");
    }

    public static void openWindow(String page, String title) throws IOException {
        FXMLLoader fx = new FXMLLoader(ManagerWindow.class.getResource("/fxml/" + page + ".fxml"));
        Scene s = new Scene((Parent) fx.load());
        Stage st = new Stage();
        st.setTitle(title);
        st.setScene(s);
        st.show();
    }

    public static void closeWindow(ActionEvent event){
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public static void openModal(ActionEvent event, String page, String title) throws IOException {
        FXMLLoader fx = new FXMLLoader(ManagerWindow.class.getResource("/fxml/" + page + ".fxml"));
        Scene s = new Scene((Parent) fx.load());
        Stage st = new Stage();
        st.setTitle(title);
        st.setScene(s);
        st.initModality(Modality.APPLICATION_MODAL);
        st.initOwner(((Node) event.getSource()).getScene().getWindow() );
        st.show();
    }

    public static void closeWindow(Stage stage){
        stage.close();
    }
}

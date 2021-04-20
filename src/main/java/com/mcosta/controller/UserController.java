package com.mcosta.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mcosta.domain.dao.UserDao;
import com.mcosta.domain.enumeration.UserTypeEnum;
import com.mcosta.domain.model.User;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.MessageAlert;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class UserController extends AccessProviderController implements Initializable {

    private UserDao userDao = new UserDao();
    private User user;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUserType;

    @FXML
    private TextField inputLogin;

    @FXML
    private PasswordField inputPassword;
    
    @FXML
    private TextField inputName;
    
    @FXML
    private ComboBox<UserTypeEnum> inputUserType;

    @FXML 
    private TabPane tabPane;

    @FXML
    private TableView tableView;

    @FXML
    private void onClickSave(ActionEvent event) {
        String username = inputLogin.getText();
        String password = inputPassword.getText();
        String name = inputName.getText();
        UserTypeEnum userType = inputUserType.getSelectionModel().getSelectedItem();

        try {
            if(user == null) {
                user = new User(username, password, name, userType);
                userDao.create(user);
                new MessageAlert("Sucesso", "Usuário cadastrado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                user.setUsername(username);
                user.setPassword(password);
                user.setName(name);
                user.setUserType(userType);
                userDao.update(user);
                new MessageAlert("Sucesso", "Usuário atualizado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            updateTable();
            goToTab(0);
            clear();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(AccessProvider.getUser().getName());
        lblUserType.setText(AccessProvider.getUser().getUserType().getValue());
        getUserTypes();
        populateTableView();
    }

    private void getUserTypes(){
        ObservableList<UserTypeEnum> obs = FXCollections.observableArrayList(UserTypeEnum.values());
        inputUserType.setItems(obs);
    }

    private void clear(){
        user = null;
        inputLogin.setText("");
        inputPassword.setText("");
        inputName.setText("");
        inputUserType.getSelectionModel().clearSelection();
    }

    private void goToTab(int index){
        tabPane.getSelectionModel().select(index);
    }

    private void populateTableView(){
        Double widthColumn = tableView.prefWidthProperty().divide(3 + 0.40).getValue();
        
        TableColumn columnUsername = new TableColumn("USUÁRIO");
        columnUsername.setMinWidth(widthColumn);
        columnUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        TableColumn columnName = new TableColumn("NOME");
        columnName.setMinWidth(widthColumn);
        columnName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        TableColumn columnUserType = new TableColumn("TIPO DE USUÁRIO");
        columnUserType.setMinWidth(widthColumn);
        columnUserType.setCellValueFactory(new PropertyValueFactory<User, String>("userType"));

        tableView.getColumns().addAll(columnUsername, columnName, columnUserType);

        addButtonsToTable();

        updateTable();
    }

    private void updateTable() {
        ObservableList<User> obs = FXCollections.observableArrayList(userDao.index());
        tableView.setItems(obs);
        tableView.refresh();
    }

    private void addButtonsToTable() {
        TableColumn<User, Void> colBtnUpdate = new TableColumn();
        colBtnUpdate.setMinWidth(50);
        TableColumn<User, Void> colBtnDelete = new TableColumn();
        colBtnDelete.setMinWidth(50);


        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactoryUpdate = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btnUpdate = new Button("Editar");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            User data = getTableView().getItems().get(getIndex());
                            onClickSelectForUpdate(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnUpdate);
                        }
                    }
                };
                return cell;
            }
        };

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactoryDelete = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btnDelete = new Button("Excluir");

                    {

                        btnDelete.setOnAction((ActionEvent event) -> {
                            User data = getTableView().getItems().get(getIndex());
                            onClickSelectForDelete(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                        }
                    }
                };
                return cell;
            }
        };

        colBtnUpdate.setCellFactory(cellFactoryUpdate);
        colBtnDelete.setCellFactory(cellFactoryDelete);

        tableView.getColumns().addAll(colBtnUpdate, colBtnDelete);
    }

    private void onClickSelectForUpdate(User _user){
        this.user = _user;
        inputLogin.setText(user.getUsername());
        inputPassword.setText(user.getPassword());
        inputName.setText(user.getName());
        inputUserType.getSelectionModel().select(user.getUserType());
        goToTab(1);
    }

    private void onClickSelectForDelete(User _user) {
        this.user = _user;
        try {
            userDao.delete(user);
            updateTable();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }
}

package com.mcosta.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.ManagerWindow;
import com.mcosta.util.MessageAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

public class ClientController extends AccessProviderController implements Initializable {


    private ClientDao clientDao = new ClientDao();
    private Physical physical;
    private Legal legal;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUserType;

    @FXML
    private TextField inputNamePhysical;

    @FXML
    private TextField inputCpfPhysical;
    
    @FXML
    private TextField inputPhonePhysical;

    @FXML
    private TextField inputAddressPhysical;

    @FXML
    private TextField inputCompanyNameLegal;

    @FXML
    private TextField inputCnpjLegal;
    
    @FXML
    private TextField inputPhoneLegal;

    @FXML
    private TextField inputAddressLegal;

    @FXML 
    private TabPane tabPane;

    @FXML
    private TableView tableViewPhysical;

    @FXML
    private TableView tableViewLegal;
    
    @FXML
    private void onClickSavePhysical(ActionEvent event) {
        String name = inputNamePhysical.getText();
        String cpf = inputCpfPhysical.getText();
        String phone = inputPhonePhysical.getText();
        String address = inputAddressPhysical.getText();

        try {
            if(physical == null) {
                physical = new Physical(name, cpf, phone, address);
                clientDao.create(physical);
                new MessageAlert("Sucesso", "Pessoa Física cadastrado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                physical.setName(name);
                physical.setCpf(cpf);
                physical.setPhone(phone);
                physical.setAddress(address);
                clientDao.update(physical);
                new MessageAlert("Sucesso", "Pessoa Física atualizada com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            updateTable();
            goToTab(0);
            clearPhysical();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }

    @FXML
    private void onClickSaveLegal(ActionEvent event) {
        String companyName = inputCompanyNameLegal.getText();
        String cnpj = inputCnpjLegal.getText();
        String phone = inputPhoneLegal.getText();
        String address = inputAddressLegal.getText();

        try {
            if(legal == null) {
                legal = new Legal(companyName, cnpj, phone, address);
                clientDao.create(legal);
                new MessageAlert("Sucesso", "Pessoa Jurídica cadastrado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                legal.setCompanyName(companyName);
                legal.setCnpj(cnpj);
                legal.setPhone(phone);
                legal.setAddress(address);
                clientDao.update(legal);
                new MessageAlert("Sucesso", "Pessoa Jurídica atualizada com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            updateTable();
            goToTab(2);
            clearLegal();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(AccessProvider.getUser().getName());
        lblUserType.setText(AccessProvider.getUser().getUserType().getValue());
        populateTableView();
    }

    private void clearPhysical(){
        physical = null;
        inputNamePhysical.setText("");
        inputCpfPhysical.setText("");
        inputPhonePhysical.setText("");
        inputAddressPhysical.setText("");
    }

    private void clearLegal(){
        legal = null;
        inputCompanyNameLegal.setText("");
        inputCnpjLegal.setText("");
        inputPhoneLegal.setText("");
        inputAddressLegal.setText("");
    }

    private void goToTab(int index){
        tabPane.getSelectionModel().select(index);
    }

    private void populateTableView(){
        Double widthColumn = tableViewPhysical.prefWidthProperty().divide(4 + 0.55).getValue();
        
        TableColumn columnName = new TableColumn("NOME");
        columnName.setMinWidth(widthColumn);
        columnName.setCellValueFactory(new PropertyValueFactory<Physical, String>("name"));

        TableColumn columnCpf = new TableColumn("CPF");
        columnCpf.setMinWidth(widthColumn);
        columnCpf.setCellValueFactory(new PropertyValueFactory<Physical, String>("cpf"));

        TableColumn columnPhone1 = new TableColumn("TELEFONE");
        columnPhone1.setMinWidth(widthColumn);
        columnPhone1.setCellValueFactory(new PropertyValueFactory<Client, String>("phone"));

        TableColumn columnAddress1 = new TableColumn("ENDEREÇO");
        columnAddress1.setMinWidth(widthColumn);
        columnAddress1.setCellValueFactory(new PropertyValueFactory<Physical, String>("address"));

        TableColumn columnCompanyName = new TableColumn("RAZÃO SOCIAL");
        columnCompanyName.setMinWidth(widthColumn);
        columnCompanyName.setCellValueFactory(new PropertyValueFactory<Legal, String>("companyName"));

        TableColumn columnCnpj = new TableColumn("CPF");
        columnCnpj.setMinWidth(widthColumn);
        columnCnpj.setCellValueFactory(new PropertyValueFactory<Legal, String>("cnpj"));

        TableColumn columnPhone2 = new TableColumn("TELEFONE");
        columnPhone2.setMinWidth(widthColumn);
        columnPhone2.setCellValueFactory(new PropertyValueFactory<Legal, String>("phone"));

        TableColumn columnAddress2 = new TableColumn("ENDEREÇO");
        columnAddress2.setMinWidth(widthColumn);
        columnAddress2.setCellValueFactory(new PropertyValueFactory<Legal, String>("address"));

        tableViewPhysical.getColumns().addAll(columnName, columnCpf, columnPhone1, columnAddress1);
        tableViewLegal.getColumns().addAll(columnCompanyName, columnCnpj, columnPhone2, columnAddress2);

        addButtonsToTablePhysical();
        addButtonsToTableLegal();

        updateTable();
    }

    private void updateTable() {
        ObservableList<Physical> obs1 = FXCollections.observableArrayList(clientDao.getPhysicals());
        tableViewPhysical.setItems(obs1);
        tableViewPhysical.refresh();

        ObservableList<Legal> obs2 = FXCollections.observableArrayList(clientDao.getLegals());
        tableViewLegal.setItems(obs2);
        tableViewLegal.refresh();
    }

    private void addButtonsToTablePhysical() {
        TableColumn<Physical, Void> colBtnUpdate = new TableColumn();
        colBtnUpdate.setMinWidth(50);
        TableColumn<Physical, Void> colBtnDelete = new TableColumn();
        colBtnDelete.setMinWidth(50);


        Callback<TableColumn<Physical, Void>, TableCell<Physical, Void>> cellFactoryUpdate = new Callback<TableColumn<Physical, Void>, TableCell<Physical, Void>>() {
            @Override
            public TableCell<Physical, Void> call(final TableColumn<Physical, Void> param) {
                final TableCell<Physical, Void> cell = new TableCell<Physical, Void>() {

                    private final Button btnUpdate = new Button("Editar");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            Physical data = getTableView().getItems().get(getIndex());
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

        Callback<TableColumn<Physical, Void>, TableCell<Physical, Void>> cellFactoryDelete = new Callback<TableColumn<Physical, Void>, TableCell<Physical, Void>>() {
            @Override
            public TableCell<Physical, Void> call(final TableColumn<Physical, Void> param) {
                final TableCell<Physical, Void> cell = new TableCell<Physical, Void>() {

                    private final Button btnDelete = new Button("Excluir");

                    {

                        btnDelete.setOnAction((ActionEvent event) -> {
                            Physical data = getTableView().getItems().get(getIndex());
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

        tableViewPhysical.getColumns().addAll(colBtnUpdate, colBtnDelete);
    }

    private void addButtonsToTableLegal() {
        TableColumn<Legal, Void> colBtnUpdate = new TableColumn();
        colBtnUpdate.setMinWidth(50);
        TableColumn<Legal, Void> colBtnDelete = new TableColumn();
        colBtnDelete.setMinWidth(50);


        Callback<TableColumn<Legal, Void>, TableCell<Legal, Void>> cellFactoryUpdate = new Callback<TableColumn<Legal, Void>, TableCell<Legal, Void>>() {
            @Override
            public TableCell<Legal, Void> call(final TableColumn<Legal, Void> param) {
                final TableCell<Legal, Void> cell = new TableCell<Legal, Void>() {

                    private final Button btnUpdate = new Button("Editar");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            Legal data = getTableView().getItems().get(getIndex());
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

        Callback<TableColumn<Legal, Void>, TableCell<Legal, Void>> cellFactoryDelete = new Callback<TableColumn<Legal, Void>, TableCell<Legal, Void>>() {
            @Override
            public TableCell<Legal, Void> call(final TableColumn<Legal, Void> param) {
                final TableCell<Legal, Void> cell = new TableCell<Legal, Void>() {

                    private final Button btnDelete = new Button("Excluir");

                    {

                        btnDelete.setOnAction((ActionEvent event) -> {
                            Legal data = getTableView().getItems().get(getIndex());
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

        tableViewLegal.getColumns().addAll(colBtnUpdate, colBtnDelete);
    }

    private void onClickSelectForUpdate(Object object){

        if(object instanceof Physical){
            this.physical = (Physical) object;
            inputNamePhysical.setText(physical.getName());
            inputCpfPhysical.setText(physical.getCpf());
            inputPhonePhysical.setText(physical.getPhone());
            inputAddressPhysical.setText(physical.getAddress());
            goToTab(1);
        }

        if(object instanceof Legal) {
            this.legal = (Legal) object;
            inputCompanyNameLegal.setText(legal.getCompanyName());
            inputCnpjLegal.setText(legal.getCnpj());
            inputPhoneLegal.setText(legal.getPhone());
            inputAddressLegal.setText(legal.getAddress());
            goToTab(3);
        }
    }

    private void onClickSelectForDelete(Object object) {
        try {
            clientDao.delete(object);
            updateTable();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }
    
}

package com.mcosta.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.dao.ContractDao;
import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Contract;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.MessageAlert;
import com.thoughtworks.xstream.converters.basic.StringConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ContractController extends AccessProviderController implements Initializable {
    
    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUserType;

    @FXML
    private DatePicker inputEffectiveStartDate;

    @FXML
    private ComboBox<Integer> inputDurationMonth;

    @FXML
    private ComboBox<Client> inputClient;

    @FXML
    private ComboBox<Client> inputEquipament;

    @FXML 
    private TabPane tabPane;

    @FXML
    private TableView tableView;

    private Contract contract;

    private ContractDao contractDao = new ContractDao();
    private ClientDao clientDao = new ClientDao();

    @FXML
    private void onClickSave(ActionEvent event) {
        LocalDate effectiveStartDate = inputEffectiveStartDate.getValue();
        Integer durationMonths = inputDurationMonth.getSelectionModel().getSelectedItem();
        Client client = inputClient.getSelectionModel().getSelectedItem();

        try {
            if(contract == null) {
                contract = new Contract(effectiveStartDate, durationMonths, client);
                contractDao.create(contract);
                new MessageAlert("Sucesso", "Contrato cadastrado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                contract.setEffectiveStartDate(effectiveStartDate);
                contract.setDurationInMonths(durationMonths);
                contract.setClient(client);
                contractDao.update(contract);
                new MessageAlert("Sucesso", "Contrato atualizado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
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
        lblUsername.setText(AccessProvider.getUsername());
        lblUserType.setText(AccessProvider.getUserType());

        populateTableView();
        getClients();
        getMonths();
    }    

    private void clear(){
        contract = null;
        inputEffectiveStartDate.setValue(null);
        inputDurationMonth.getSelectionModel().clearSelection();
        inputClient.getSelectionModel().clearSelection();
        // inputEquipament
    }

    private void getMonths(){
        List<Integer> months = new ArrayList<>();
        for(int i = 0; i < 12; i++) months.add(i+1);
        ObservableList<Integer> obs = FXCollections.observableArrayList(months);
        inputDurationMonth.setItems(obs);
    }

    private void getClients(){
        List<Client> clients = clientDao.index();
        ObservableList<Client> obs = FXCollections.observableArrayList(clients);
        inputClient.setItems(obs);
        inputClient.setCellFactory(new Callback<ListView<Client>,ListCell<Client>>(){

            @Override
            public ListCell<Client> call(ListView<Client> p) {
                
                final ListCell<Client> cell = new ListCell<Client>(){

                    @Override
                    protected void updateItem(Client _client, boolean bln) {
                        super.updateItem(_client, bln);
                        setText(getValueComboBox(_client));
                    }
 
                };
                
                return cell;
            }
        });
    }

    private String getValueComboBox(Object object){
        if(object instanceof Physical) {
            Physical p = (Physical) object;
            return p.getCpf() + " - " + p.getName();            
        }
        if(object instanceof Legal) {
            Legal l = (Legal) object;
            return l.getCnpj() + " - " + l.getCompanyName();
        }
        return "";
    }

    private void populateTableView(){
        Double widthColumn = tableView.prefWidthProperty().divide(2 + 0.55).getValue();
        
        TableColumn columnName = new TableColumn("DATA DE INÍCIO");
        columnName.setMinWidth(widthColumn);
        columnName.setCellValueFactory(new PropertyValueFactory<Contract, LocalDate>("effectiveStartDate"));

        TableColumn columnDuration = new TableColumn("DURAÇÃO (MESES)");
        columnDuration.setMinWidth(widthColumn);
        columnDuration.setCellValueFactory(new PropertyValueFactory<Contract, Integer>("durationInMonths"));

        tableView.getColumns().addAll(columnName, columnDuration);

        addButtonsToTable();

        updateTable();
    }

    private void addButtonsToTable() {
        TableColumn<Contract, Void> colBtnUpdate = new TableColumn();
        colBtnUpdate.setMinWidth(50);
        TableColumn<Contract, Void> colBtnDelete = new TableColumn();
        colBtnDelete.setMinWidth(50);


        Callback<TableColumn<Contract, Void>, TableCell<Contract, Void>> cellFactoryUpdate = new Callback<TableColumn<Contract, Void>, TableCell<Contract, Void>>() {
            @Override
            public TableCell<Contract, Void> call(final TableColumn<Contract, Void> param) {
                final TableCell<Contract, Void> cell = new TableCell<Contract, Void>() {

                    private final Button btnUpdate = new Button("Editar");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            Contract data = getTableView().getItems().get(getIndex());
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

        Callback<TableColumn<Contract, Void>, TableCell<Contract, Void>> cellFactoryDelete = new Callback<TableColumn<Contract, Void>, TableCell<Contract, Void>>() {
            @Override
            public TableCell<Contract, Void> call(final TableColumn<Contract, Void> param) {
                final TableCell<Contract, Void> cell = new TableCell<Contract, Void>() {

                    private final Button btnDelete = new Button("Excluir");

                    {

                        btnDelete.setOnAction((ActionEvent event) -> {
                            Contract data = getTableView().getItems().get(getIndex());
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

    private void onClickSelectForUpdate(Object object){

        this.contract = (Contract) object;
        inputEffectiveStartDate.setValue(contract.getEffectiveStartDate());
        inputDurationMonth.getSelectionModel().select(contract.getDurationInMonths());
        inputClient.getSelectionModel().select(contract.getClient());
        //inputEquipament
        goToTab(1);
    }

    private void onClickSelectForDelete(Object object) {
        try {
            clientDao.delete(object);
            updateTable();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }

    private void goToTab(int index){
        tabPane.getSelectionModel().select(index);
    }

    private void updateTable() {
        ObservableList<Contract> obs = FXCollections.observableArrayList(contractDao.index());
        tableView.setItems(obs);
        tableView.refresh();
    }
}

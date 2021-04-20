package com.mcosta.controller;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.dao.ContractDao;
import com.mcosta.domain.dao.EquipmentDao;
import com.mcosta.domain.dao.UserDao;
import com.mcosta.domain.enumeration.UserTypeEnum;
import com.mcosta.domain.model.*;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.MessageAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EquipmentController extends AccessProviderController implements Initializable {

    private EquipmentDao equipmentDao = new EquipmentDao();
    private Equipment equipment;
    private ClientDao clientDao = new ClientDao();
    private ContractDao contractDao = new ContractDao();

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUserType;

    @FXML
    private TextField inputDescription;

    @FXML
    private TextField inputBrand;
    
    @FXML
    private TextField inputModel;

    @FXML
    private TextField inputSerialNumber;

    @FXML
    private ComboBox<Client> inputClient;

    @FXML
    private ComboBox<Contract> inputContract;

    @FXML 
    private TabPane tabPane;

    @FXML
    private TableView tableView;

    @FXML
    private void onClickSave(ActionEvent event) {
        String description = inputDescription.getText();
        String brand = inputBrand.getText();
        String model = inputModel.getText();
        String serialNumber = inputSerialNumber.getText();
        Contract contract = inputContract.getSelectionModel().getSelectedItem();

        try {
            if(equipment == null) {
                equipment = new Equipment(description, brand, model, serialNumber, contract);
                equipmentDao.create(equipment);
                new MessageAlert("Sucesso", "Equipamento cadastrado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                equipment.setDescription(description);
                equipment.setBrand(brand);
                equipment.setModel(model);
                equipment.setSerialNumber(serialNumber);
                equipment.setContract(contract);
                equipmentDao.update(equipment);
                new MessageAlert("Sucesso", "Equipamento atualizado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            updateTable();
            goToTab(0);
            clear();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
    }

    @FXML
    private void onSelectClient(ActionEvent event) {
        Client client = inputClient.getSelectionModel().getSelectedItem();
        if(client != null) getContracts(client.getIdClient());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(AccessProvider.getUser().getName());
        lblUserType.setText(AccessProvider.getUser().getUserType().getValue());
        getClients();
        populateTableView();
    }

    private void clear(){
        equipment = null;
        inputDescription.setText("");
        inputBrand.setText("");
        inputModel.setText("");
        inputSerialNumber.setText("");
        inputContract.getSelectionModel().select(null);
        inputClient.getSelectionModel().select(null);
    }

    private void getContracts(Long idClient){
        List<Contract> contracts = contractDao.index(idClient);
        ObservableList<Contract> obs = FXCollections.observableArrayList(contracts);
        inputContract.setItems(obs);
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

    private void goToTab(int index){
        tabPane.getSelectionModel().select(index);
    }

    private void populateTableView(){
        Double widthColumn = tableView.prefWidthProperty().divide(4 + 0.20).getValue();
        
        TableColumn columnDescription = new TableColumn("DESCRIÇÃO");
        columnDescription.setMinWidth(widthColumn);
        columnDescription.setCellValueFactory(new PropertyValueFactory<Equipment, String>("description"));

        TableColumn columnBrand = new TableColumn("MARCA");
        columnBrand.setMinWidth(widthColumn);
        columnBrand.setCellValueFactory(new PropertyValueFactory<Equipment, String>("brand"));

        TableColumn columnModel = new TableColumn("MODELO");
        columnModel.setMinWidth(widthColumn);
        columnModel.setCellValueFactory(new PropertyValueFactory<Equipment, String>("model"));

        TableColumn columnSerialNumber = new TableColumn("NÚMERO DE SÉRIE");
        columnSerialNumber.setMinWidth(widthColumn);
        columnSerialNumber.setCellValueFactory(new PropertyValueFactory<Equipment, String>("serialNumber"));


        tableView.getColumns().addAll(columnDescription, columnBrand, columnModel, columnSerialNumber);

        addButtonsToTable();

        updateTable();
    }

    private void updateTable() {
        ObservableList<Equipment> obs = FXCollections.observableArrayList(equipmentDao.index());
        tableView.setItems(obs);
        tableView.refresh();
    }

    private void addButtonsToTable() {
        TableColumn<Equipment, Void> colBtnUpdate = new TableColumn();
        colBtnUpdate.setMinWidth(50);

        Callback<TableColumn<Equipment, Void>, TableCell<Equipment, Void>> cellFactoryUpdate = new Callback<TableColumn<Equipment, Void>, TableCell<Equipment, Void>>() {
            @Override
            public TableCell<Equipment, Void> call(final TableColumn<Equipment, Void> param) {
                final TableCell<Equipment, Void> cell = new TableCell<Equipment, Void>() {

                    private final Button btnUpdate = new Button("Editar");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            Equipment data = getTableView().getItems().get(getIndex());
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

        colBtnUpdate.setCellFactory(cellFactoryUpdate);

        tableView.getColumns().addAll(colBtnUpdate);
    }

    private void onClickSelectForUpdate(Equipment _equipment){
        this.equipment = _equipment;
        inputDescription.setText(equipment.getDescription());
        inputBrand.setText(equipment.getBrand());
        inputModel.setText(equipment.getModel());
        inputSerialNumber.setText(equipment.getSerialNumber());
        inputContract.getSelectionModel().select(equipment.getContract());
        inputClient.getSelectionModel().select(equipment.getContract().getClient());
        goToTab(1);
    }

}

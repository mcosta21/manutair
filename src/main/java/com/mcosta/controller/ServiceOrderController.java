package com.mcosta.controller;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.dao.ContractDao;
import com.mcosta.domain.dao.EquipmentDao;
import com.mcosta.domain.dao.ServiceOrderDao;
import com.mcosta.domain.model.*;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.DateFormatter;
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
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceOrderController extends AccessProviderController implements Initializable {


    private ServiceOrder serviceOrder;
    private ClientDao clientDao = new ClientDao();
    private ContractDao contractDao = new ContractDao();
    private EquipmentDao equipmentDao = new EquipmentDao();
    private ServiceOrderDao serviceOrderDao = new ServiceOrderDao();

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUserType;

    @FXML
    private TextArea inputProblemDescription;

    @FXML
    private ComboBox<Client> inputClient;

    @FXML
    private ComboBox<Equipment> inputEquipment;

    @FXML
    private ComboBox<Contract> inputContract;

    @FXML
    private TextField inputAddressEquipment;

    @FXML
    private Label valueCreationDate;

    @FXML
    private Label valueIdServiceOrder;

    @FXML
    private Label valueEquipmentAddress;

    @FXML
    private DatePicker inputServiceDate;

    @FXML
    private Label valueClient;

    @FXML
    private Label valueContract;

    @FXML
    private Label valueEquipment;

    @FXML
    private ComboBox<User> inputTechnician;

    @FXML 
    private TabPane tabPane;

    @FXML
    private TableView tableView;

    @FXML
    private void onClickSave(ActionEvent event) {
        Equipment equipment = inputEquipment.getSelectionModel().getSelectedItem();
        String equipmentLocationAddress = inputAddressEquipment.getText();
        String problemDescription = inputProblemDescription.getText();

        try {
            if(serviceOrder == null) {
                serviceOrder = new ServiceOrder(equipmentLocationAddress, problemDescription, equipment);
                serviceOrderDao.create(serviceOrder);
                new MessageAlert("Sucesso", "Ordem de serviço aberta com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                serviceOrder.setEquipment(equipment);
                serviceOrder.setEquipmentLocationAddress(equipmentLocationAddress);
                serviceOrder.setProblemDescription(problemDescription);
                serviceOrderDao.update(serviceOrder);
                new MessageAlert("Sucesso", "Ordem de serviço atualizada com sucesso.", AlertType.INFORMATION).sendMessageAlert();
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

    @FXML
    private void onSelectContract(ActionEvent event) {
        Contract contract = inputContract.getSelectionModel().getSelectedItem();
        if(contract != null) getEquipments(contract.getIdContract());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(AccessProvider.getUser().getName());
        lblUserType.setText(AccessProvider.getUser().getUserType().getValue());
        getClients();
        populateTableView();
        managerTabs();
    }

    private void managerTabs(){
        switch(AccessProvider.getUser().getUserType()) {
            case ATTENDANT:
                tabPane.getTabs().remove(2);
                break;
            case SUPERVISOR:
                tabPane.getTabs().remove(1);
                Tab tabAllocateTechnician = tabPane.getTabs().get(1);
                tabAllocateTechnician.setDisable(true);
                break;
            case TECHNICIAN:
                tabPane.getTabs().remove(0);
                tabPane.getTabs().remove(0);
                break;
            case ADMIN:
                break;
        }
    }

    private void clear(){
        serviceOrder = null;
        inputAddressEquipment.setText("");
        inputProblemDescription.setText("");
        inputContract.getSelectionModel().select(null);
        inputClient.getSelectionModel().select(null);
        inputEquipment.getSelectionModel().select(null);
    }

    private void getEquipments(Long idContract){
        List<Equipment> equipments = equipmentDao.index(idContract);
        ObservableList<Equipment> obs = FXCollections.observableArrayList(equipments);
        inputEquipment.setItems(obs);
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

    private void getTechnicians(){
        //buscar usuários de tipo tecnico e is technico true
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
        TableColumn columnIdServiceOrder = new TableColumn("ORDEM DE SERVIÇO");
        columnIdServiceOrder.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("IdServiceOrder"));

        TableColumn columnCreationDate = new TableColumn("DATA DE ABERTURA");
        columnCreationDate.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("creationDateFormatted"));

        TableColumn columnEquipment = new TableColumn("EQUIPAMENTO");
        columnEquipment.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("descriptionEquipment"));

        TableColumn columnClient = new TableColumn("CLIENT");
        columnClient.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("clientName"));

        TableColumn columnContract = new TableColumn("CONTRATO");
        columnContract.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("idContract"));

        tableView.getColumns().addAll(columnIdServiceOrder, columnCreationDate, columnEquipment, columnClient, columnContract);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addButtonsToTable();

        updateTable();
    }

    private void updateTable() {
        // FILTRAR POR TIPO DE USUÁRIO
        ObservableList<ServiceOrder> obs = FXCollections.observableArrayList(serviceOrderDao.index());
        tableView.setItems(obs);
        tableView.refresh();
    }

    private void addButtonsToTable() {
        switch(AccessProvider.getUser().getUserType()) {
            case ATTENDANT:
                addButtonsToTableForAttendant();
                break;
            case SUPERVISOR:
                addButtonsToTableForSupervisor();
                break;
            case TECHNICIAN:
                break;
            case ADMIN:
                break;
        }
    }

    private void addButtonsToTableForAttendant() {
        TableColumn<ServiceOrder, Void> colBtnUpdate = new TableColumn();
        colBtnUpdate.setMinWidth(50);
        Callback<TableColumn<ServiceOrder, Void>, TableCell<ServiceOrder, Void>> cellFactoryUpdate = new Callback<TableColumn<ServiceOrder, Void>, TableCell<ServiceOrder, Void>>() {
            @Override
            public TableCell<ServiceOrder, Void> call(final TableColumn<ServiceOrder, Void> param) {
                final TableCell<ServiceOrder, Void> cell = new TableCell<ServiceOrder, Void>() {

                    private final Button btnUpdate = new Button("Editar");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            ServiceOrder data = getTableView().getItems().get(getIndex());
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

    private void addButtonsToTableForSupervisor() {
        TableColumn<ServiceOrder, Void> colBtnAllocateTechnician = new TableColumn();
        colBtnAllocateTechnician.setMinWidth(50);
        Callback<TableColumn<ServiceOrder, Void>, TableCell<ServiceOrder, Void>> cellFactoryAllocateTechnician = new Callback<TableColumn<ServiceOrder, Void>, TableCell<ServiceOrder, Void>>() {
            @Override
            public TableCell<ServiceOrder, Void> call(final TableColumn<ServiceOrder, Void> param) {
                final TableCell<ServiceOrder, Void> cell = new TableCell<ServiceOrder, Void>() {

                    private final Button btnAllocateTechnician = new Button("Alocar Técnico");

                    {
                        btnAllocateTechnician.setOnAction((ActionEvent event) -> {
                            ServiceOrder data = getTableView().getItems().get(getIndex());
                            onClickAllocateTechnician(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnAllocateTechnician);
                        }
                    }
                };
                return cell;
            }
        };
        colBtnAllocateTechnician.setCellFactory(cellFactoryAllocateTechnician);
        tableView.getColumns().addAll(colBtnAllocateTechnician);
    }

    private void onClickSelectForUpdate(ServiceOrder _serviceOrder){
        this.serviceOrder = _serviceOrder;
        inputAddressEquipment.setText(serviceOrder.getEquipmentLocationAddress());
        inputProblemDescription.setText(serviceOrder.getProblemDescription());
        inputContract.getSelectionModel().select(serviceOrder.getEquipment().getContract());
        inputClient.getSelectionModel().select(serviceOrder.getEquipment().getContract().getClient());
        inputEquipment.getSelectionModel().select(serviceOrder.getEquipment());
        goToTab(1);
    }

    private void onClickAllocateTechnician(ServiceOrder _serviceOrder){
        Tab tabServiceOrders = tabPane.getTabs().get(0);
        tabServiceOrders.setDisable(true);
        Tab tabAllocateTechnician = tabPane.getTabs().get(1);
        tabAllocateTechnician.setDisable(false);
        this.serviceOrder = _serviceOrder;
        valueClient.setText(serviceOrder.getClientName());
        valueContract.setText(String.valueOf(serviceOrder.getIdContract()));
        valueCreationDate.setText(DateFormatter.LocalDateTimeFormatted(serviceOrder.getCreationDate()));
        valueEquipment.setText(serviceOrder.getEquipment().getDescription());
        valueIdServiceOrder.setText(String.valueOf(serviceOrder.getIdServiceOrder()));
        valueEquipmentAddress.setText(serviceOrder.getEquipmentLocationAddress());
        goToTab(1);
    }

    @FXML
    private void onClickAllocateTechnician(ActionEvent event) {

    }

    @FXML
    private void onClickCancelAllocateTechnician(ActionEvent event) {
        this.serviceOrder = null;
        Tab tabServiceOrders = tabPane.getTabs().get(0);
        tabServiceOrders.setDisable(false);
        Tab tabAllocateTechnician = tabPane.getTabs().get(1);
        tabAllocateTechnician.setDisable(true);
        goToTab(0);
    }




}

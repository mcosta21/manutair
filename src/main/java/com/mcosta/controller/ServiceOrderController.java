package com.mcosta.controller;

import com.mcosta.domain.dao.*;
import com.mcosta.domain.enumeration.StatusServiceOrderEnum;
import com.mcosta.domain.model.*;
import com.mcosta.domain.validator.ServiceOrderValidator;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.DateFormatter;
import com.mcosta.util.MaskedTextField;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceOrderController extends AccessProviderController implements Initializable {


    private ServiceOrder serviceOrder;
    private ClientDao clientDao = new ClientDao();
    private ContractDao contractDao = new ContractDao();
    private EquipmentDao equipmentDao = new EquipmentDao();
    private ServiceOrderDao serviceOrderDao = new ServiceOrderDao();
    private UserDao userDao = new UserDao();
    private ServiceOrderValidator serviceOrderValidator = new ServiceOrderValidator();

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
    private DatePicker inputServiceDate;

    @FXML
    private MaskedTextField inputTime;
    //private TextField inputTime;

    @FXML
    private Label valueCreationDate;
    @FXML
    private Label valueCreationDate1;

    @FXML
    private Label valueIdServiceOrder;
    @FXML
    private Label valueIdServiceOrder1;

    @FXML
    private Label valueEquipmentAddress;
    @FXML
    private Label valueEquipmentAddress1;

    @FXML
    private Label valueClient;
    @FXML
    private Label valueClient1;

    @FXML
    private Label valueContract;
    @FXML
    private Label valueContract1;

    @FXML
    private Label valueEquipment;
    @FXML
    private Label valueEquipment1;

    @FXML
    private ComboBox<User> inputTechnician;

    @FXML
    private DatePicker inputFinishDate;

    @FXML
    private TextArea inputSolutionDescription;

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
            serviceOrder = null;
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
        getTechnicians();
        populateTableView();
        managerTabs();

        inputTime.setMask("##:##");
    }

    private void managerTabs(){
        switch(AccessProvider.getUser().getUserType()) {
            case ATTENDANT:
                tabPane.getTabs().remove(2);
                tabPane.getTabs().remove(2);
                break;
            case SUPERVISOR:
                tabPane.getTabs().remove(1);
                Tab tabAllocateTechnician = tabPane.getTabs().get(1);
                tabAllocateTechnician.setDisable(true);
                tabPane.getTabs().remove(2);
                break;
            case TECHNICIAN:
                tabPane.getTabs().remove(1);
                tabPane.getTabs().remove(1);
                Tab tabFinishServiceOrder = tabPane.getTabs().get(1);
                tabFinishServiceOrder.setDisable(true);
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
        List<User> technicians = userDao.getUsersTechnician();
        ObservableList<User> obs = FXCollections.observableArrayList(technicians);
        inputTechnician.setItems(obs);
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

        TableColumn columnStatus = new TableColumn("STATUS");
        columnStatus.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("statusServiceOrder"));

        TableColumn columnEquipment = new TableColumn("EQUIPAMENTO");
        columnEquipment.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("descriptionEquipment"));

        TableColumn columnClient = new TableColumn("CLIENTE");
        columnClient.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("clientName"));

        TableColumn columnContract = new TableColumn("CONTRATO");
        columnContract.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("idContract"));

        tableView.getColumns().addAll(columnIdServiceOrder, columnCreationDate, columnStatus, columnEquipment, columnClient, columnContract);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addButtonsToTable();

        updateTable();
    }

    private void updateTable() {
        ObservableList<ServiceOrder> obs = null;
        switch(AccessProvider.getUser().getUserType()) {
            case ATTENDANT:
            case SUPERVISOR:
                obs = FXCollections.observableArrayList(serviceOrderDao.getServiceOrdersByStatusOpened());
                break;
            case TECHNICIAN:
                obs = FXCollections.observableArrayList(serviceOrderDao.getServiceOrdersByTechnician(AccessProvider.getUser().getIdUser()));
                break;
            case ADMIN:
                obs = FXCollections.observableArrayList(serviceOrderDao.index());
                break;
        }
        
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
                addButtonsToTableForTechnician();
                break;
            case ADMIN:
                addButtonsToTableForAttendant();
                addButtonsToTableForSupervisor();
                addButtonsToTableForTechnician();
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

    private void addButtonsToTableForTechnician() {
        TableColumn<ServiceOrder, Void> colBtnFinishServiceOrder = new TableColumn();
        colBtnFinishServiceOrder.setMinWidth(50);
        Callback<TableColumn<ServiceOrder, Void>, TableCell<ServiceOrder, Void>> cellFactoryFinishServiceOrder = new Callback<TableColumn<ServiceOrder, Void>, TableCell<ServiceOrder, Void>>() {
            @Override
            public TableCell<ServiceOrder, Void> call(final TableColumn<ServiceOrder, Void> param) {
                final TableCell<ServiceOrder, Void> cell = new TableCell<ServiceOrder, Void>() {

                    private final Button btnFinishServiceOrder = new Button("Finalizar OS");

                    {
                        btnFinishServiceOrder.setOnAction((ActionEvent event) -> {
                            ServiceOrder data = getTableView().getItems().get(getIndex());
                            onClickFinishServiceOrder(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnFinishServiceOrder);
                        }
                    }
                };
                return cell;
            }
        };
        colBtnFinishServiceOrder.setCellFactory(cellFactoryFinishServiceOrder);
        tableView.getColumns().addAll(colBtnFinishServiceOrder);
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
    private void onClickSaveAllocateTechnician(ActionEvent event) throws Exception {
        User user = inputTechnician.getSelectionModel().getSelectedItem();
        LocalDate serviceDate = inputServiceDate.getValue();

        if(!serviceOrderValidator.isValidToAllocateTechnician(user, serviceDate, inputTime.getText())){
            new MessageAlert("Erro", serviceOrderValidator.getMessage()).sendMessageAlert();
            return;
        }

        LocalTime time = LocalTime.parse(inputTime.getText() + ":00.341920600");
        serviceOrder.setServiceDate(LocalDateTime.of(serviceDate, time));
        serviceOrder.setStatusServiceOrderEnum(StatusServiceOrderEnum.IN_PROGRESS);
        serviceOrder.setUser(user);


        serviceOrderDao.update(serviceOrder);
        new MessageAlert("Sucesso", "Técnico " + user.getName() + " alocado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
        updateTable();
        onClickCancelAllocateTechnician(null);
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

    private void onClickFinishServiceOrder(ServiceOrder _serviceOrder){
        Tab tabServiceOrders = tabPane.getTabs().get(0);
        tabServiceOrders.setDisable(true);
        Tab tabFinishServiceOrder = tabPane.getTabs().get(1);
        tabFinishServiceOrder.setDisable(false);
        this.serviceOrder = _serviceOrder;
        valueClient1.setText(serviceOrder.getClientName());
        valueContract1.setText(String.valueOf(serviceOrder.getIdContract()));
        valueCreationDate1.setText(DateFormatter.LocalDateTimeFormatted(serviceOrder.getCreationDate()));
        valueEquipment1.setText(serviceOrder.getEquipment().getDescription());
        valueIdServiceOrder1.setText(String.valueOf(serviceOrder.getIdServiceOrder()));
        valueEquipmentAddress1.setText(serviceOrder.getEquipmentLocationAddress());
        goToTab(1);
    }

    @FXML
    void onClickSaveFinishServiceOrder(ActionEvent event) throws Exception {
        String solutionDescription = inputSolutionDescription.getText();
        LocalDate finishDate = inputFinishDate.getValue();

        serviceOrder.setSolutionDescription(solutionDescription);
        serviceOrder.setFinishDate(finishDate);
        serviceOrder.setStatusServiceOrderEnum(StatusServiceOrderEnum.CONCLUDED);

        if(!serviceOrderValidator.isValidToFinishServiceOrder(serviceOrder)){
            new MessageAlert("Erro", serviceOrderValidator.getMessage()).sendMessageAlert();
            return;
        }

        serviceOrderDao.update(serviceOrder);
        new MessageAlert("Sucesso", "Ordem de serviço " + serviceOrder.getIdServiceOrder() + " finalizada com sucesso.", AlertType.INFORMATION).sendMessageAlert();
        updateTable();
        onClickCancelFinishServiceOrder(null);
    }

    @FXML
    void onClickCancelFinishServiceOrder(ActionEvent event) {
        this.serviceOrder = null;
        Tab tabServiceOrders = tabPane.getTabs().get(0);
        tabServiceOrders.setDisable(false);
        Tab tabFinishServiceOrder = tabPane.getTabs().get(1);
        tabFinishServiceOrder.setDisable(true);
        goToTab(0);
    }


}

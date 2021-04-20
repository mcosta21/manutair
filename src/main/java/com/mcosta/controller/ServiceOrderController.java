package com.mcosta.controller;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.dao.ContractDao;
import com.mcosta.domain.dao.EquipmentDao;
import com.mcosta.domain.dao.ServiceOrderDao;
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
        lblUsername.setText(AccessProvider.getUsername());
        lblUserType.setText(AccessProvider.getUserType());
        getClients();
        populateTableView();

        managerTabs();

    }

    private void managerTabs(){
        /*
        switch() {
            case ATTENDANT:
                return pagesForAttendant();
            case SUPERVISOR:
                return pagesForSupervisor();
            case TECHNICIAN:
                return pagesForTechnician();
            case ADMIN:
                return pagesForAdmin();
        }
        */
        Tab tab = tabPane.getTabs().get(1);
        tabPane.getTabs().remove(tab);
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
        Double widthColumn = tableView.prefWidthProperty().divide(5 + 0.30).getValue();
        
        TableColumn columnIdServiceOrder = new TableColumn("ORDEM DE SERVIÇO");
        columnIdServiceOrder.setMinWidth(widthColumn);
        columnIdServiceOrder.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("IdServiceOrder"));

        TableColumn columnCreationDate = new TableColumn("DATA DE ABERTURA");
        columnCreationDate.setMinWidth(widthColumn);
        columnCreationDate.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("creationDateFormatted"));

        TableColumn columnEquipment = new TableColumn("EQUIPAMENTO");
        columnEquipment.setMinWidth(widthColumn);
        columnEquipment.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("descriptionEquipment"));

        TableColumn columnClient = new TableColumn("CLIENT");
        columnClient.setMinWidth(widthColumn);
        columnClient.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("clientName"));

        TableColumn columnContract = new TableColumn("CONTRATO");
        columnContract.setMinWidth(widthColumn);
        columnContract.setCellValueFactory(new PropertyValueFactory<ServiceOrder, String>("idContract"));

        tableView.getColumns().addAll(columnIdServiceOrder, columnCreationDate, columnEquipment, columnClient, columnContract);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addButtonsToTable();

        updateTable();
    }

    private void updateTable() {
        ObservableList<ServiceOrder> obs = FXCollections.observableArrayList(serviceOrderDao.index());
        tableView.setItems(obs);
        tableView.refresh();
    }

    private void addButtonsToTable() {
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

    private void onClickSelectForUpdate(ServiceOrder _serviceOrder){
        this.serviceOrder = _serviceOrder;
        inputAddressEquipment.setText(serviceOrder.getEquipmentLocationAddress());
        inputProblemDescription.setText(serviceOrder.getProblemDescription());
        inputContract.getSelectionModel().select(serviceOrder.getEquipment().getContract());
        inputClient.getSelectionModel().select(serviceOrder.getEquipment().getContract().getClient());
        inputEquipment.getSelectionModel().select(serviceOrder.getEquipment());
        goToTab(1);
    }

}

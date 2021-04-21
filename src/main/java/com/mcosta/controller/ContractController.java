package com.mcosta.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mcosta.domain.dao.ClientDao;
import com.mcosta.domain.dao.ContractDao;
import com.mcosta.domain.dao.EquipmentDao;
import com.mcosta.domain.model.*;
import com.mcosta.domain.validator.EquipmentValidator;
import com.mcosta.util.AccessProvider;
import com.mcosta.util.ManagerWindow;
import com.mcosta.util.MessageAlert;
import com.thoughtworks.xstream.converters.basic.StringConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ContractController extends AccessProviderController implements Initializable {

    private Contract contract;
    private ContractDao contractDao = new ContractDao();
    private ClientDao clientDao = new ClientDao();
    private EquipmentDao equipmentDao = new EquipmentDao();
    List<Equipment> equipments = new ArrayList<Equipment>();
    private EquipmentValidator equipmentValidator = new EquipmentValidator();

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
    private TextField inputDescription;

    @FXML
    private TextField inputBrand;

    @FXML
    private TextField inputModel;

    @FXML
    private TextField inputSerialNumber;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView tableView;

    @FXML
    private ListView listViewEquipaments;

    @FXML
    private Pane modalCreateEquipment;

    @FXML
    private void onClickSave(ActionEvent event) {
        LocalDate effectiveStartDate = inputEffectiveStartDate.getValue();
        Integer durationMonths = inputDurationMonth.getSelectionModel().getSelectedItem();
        Client client = inputClient.getSelectionModel().getSelectedItem();

        try {
            if(contract == null) {
                contract = new Contract(effectiveStartDate, durationMonths, client);
                contractDao.create(contract);

                for(Equipment e : equipments){
                    e.setContract(contract);
                    equipmentDao.create(e);
                }

                new MessageAlert("Sucesso", "Contrato cadastrado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            else {
                contract.setEffectiveStartDate(effectiveStartDate);
                contract.setDurationInMonths(durationMonths);
                contract.setClient(client);
                contractDao.update(contract);

                for(Equipment e : equipments){
                    if(e.getIdEquipment() == null) {
                        e.setContract(contract);
                        equipmentDao.create(e);
                    }
                    else {
                        equipmentDao.update(e);
                    }

                }

                new MessageAlert("Sucesso", "Contrato atualizado com sucesso.", AlertType.INFORMATION).sendMessageAlert();
            }
            updateTable();
            goToTab(0);
            clear();
        } catch (Exception e) {
            new MessageAlert("Erro", e.getMessage()).sendMessageAlert();
        }
     }

    @FXML
    private void onClickSaveEquipment(ActionEvent event) {
        String description = inputDescription.getText();
        String model = inputModel.getText();
        String brand = inputBrand.getText();
        String serialNumber= inputSerialNumber.getText();

        Equipment equipment = new Equipment(description, model, brand, serialNumber, null);

        if(!equipmentValidator.isValidFast(equipment)) {
            new MessageAlert("Erro", equipmentValidator.getMessage()).sendMessageAlert();
            return;
        }
        equipments.add(equipment);
        getEquipments();
        modalCreateEquipment.setVisible(false);
    }

    @FXML
    private void onClickCancelEquipment(ActionEvent event) {
        modalCreateEquipment.setVisible(false);
    }

    @FXML
    private void openModalCreateEquipment(ActionEvent event) throws IOException {
        modalCreateEquipment.setVisible(true);
        clearEquipment();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(AccessProvider.getUser().getName());
        lblUserType.setText(AccessProvider.getUser().getUserType().getValue());

        modalCreateEquipment.setVisible(false);
        modalCreateEquipment.setLayoutX(0);
        modalCreateEquipment.setLayoutY(0);

        populateTableView();
        getClients();
        getMonths();
    }    

    private void clear(){
        clear(null);
    }

    private void clearEquipment(){
        inputDescription.setText("");
        inputBrand.setText("");
        inputModel.setText("");
        inputSerialNumber.setText("");
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

    private void getEquipments() {
        ObservableList<Equipment> obs = FXCollections.observableArrayList(equipments);
        listViewEquipaments.setItems(obs);
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

        TableColumn columnIdContract = new TableColumn("N° DO CONTRATO");
        columnIdContract.setCellValueFactory(new PropertyValueFactory<Contract, LocalDate>("idContract"));

        TableColumn columnEffectiveStartDate = new TableColumn("DATA DE INÍCIO");
        columnEffectiveStartDate.setCellValueFactory(new PropertyValueFactory<Contract, String>("effectiveStartDateFormatted"));

        TableColumn columnDuration = new TableColumn("DURAÇÃO");
        columnDuration.setCellValueFactory(new PropertyValueFactory<Contract, String>("durationInMonthsText"));

        TableColumn columnClient = new TableColumn("CLIENTE");
        columnClient.setCellValueFactory(new PropertyValueFactory<Contract, String>("clientName"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(columnIdContract, columnEffectiveStartDate, columnDuration, columnClient);

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

        colBtnUpdate.setCellFactory(cellFactoryUpdate);

        tableView.getColumns().addAll(colBtnUpdate);
    }

    private void onClickSelectForUpdate(Object object){
        this.contract = (Contract) object;
        this.equipments = equipmentDao.index(contract.getIdContract());
        inputEffectiveStartDate.setValue(contract.getEffectiveStartDate());
        inputDurationMonth.getSelectionModel().select(contract.getDurationInMonths());
        inputClient.getSelectionModel().select(contract.getClient());
        getEquipments();
        goToTab(1);
    }

    private void goToTab(int index){
        tabPane.getSelectionModel().select(index);
    }

    private void updateTable() {
        ObservableList<Contract> obs = FXCollections.observableArrayList(contractDao.index());
        tableView.setItems(obs);
        tableView.refresh();
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        clear();
        goToTab(0);
    }

    @FXML
    void clear(ActionEvent event) {
        contract = null;
        equipments = new ArrayList<>();
        inputEffectiveStartDate.setValue(null);
        inputDurationMonth.getSelectionModel().clearSelection();
        inputClient.getSelectionModel().select(null);
        listViewEquipaments.setItems(null);
    }
}

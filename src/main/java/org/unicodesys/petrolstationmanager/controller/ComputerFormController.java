package org.unicodesys.petrolstationmanager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.unicodesys.petrolstationmanager.enums.ComputerType;
import org.unicodesys.petrolstationmanager.model.Computer;
import org.unicodesys.petrolstationmanager.service.ComputerService;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ComputerFormController {

    @FXML
    private Label computerIdLabel;
    @FXML
    private TextField computerNameField;
    @FXML
    private TextField ipAddressField;
    @FXML
    private ComboBox<ComputerType> computerTypeComboBox;
    @FXML
    private TextField petrolStationIdField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final ComputerService computerService = new ComputerService();
    private Computer computer;
    private boolean isEditMode = false;

    @FXML
    private void initialize() {
        computerTypeComboBox.setItems(FXCollections.observableArrayList(ComputerType.values()));
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
        if (computer != null) {
            isEditMode = true;
            computerIdLabel.setText(String.valueOf(computer.getId()));
            computerNameField.setText(computer.getNumber());
            ipAddressField.setText(computer.getIpAddress().getHostAddress());
            computerTypeComboBox.setValue(computer.getType());
            petrolStationIdField.setText(String.valueOf(computer.getPetrolStationId()));
        } else {
            isEditMode = false;
            computerIdLabel.setText("(Auto-generated)");
        }
    }

    public void setUpdateMode(Computer computer) {
        setComputer(computer);
    }

    @FXML
    private void handleSave() {
        String name = computerNameField.getText();
        String ipAddressText = ipAddressField.getText();
        ComputerType type = computerTypeComboBox.getValue();
        Integer petrolStationId;

        if (name.isEmpty() || ipAddressText.isEmpty() || type == null) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        try {
            petrolStationId = Integer.parseInt(petrolStationIdField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid Petrol Station ID. Please enter a valid number.");
            return;
        }

        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(ipAddressText);
        } catch (UnknownHostException e) {
            showAlert("Validation Error", "Invalid IP Address format. Please enter a valid IP.");
            return;
        }

        if (isEditMode) {
            computer.setNumber(name);
            computer.setIpAddress(ipAddress);
            computer.setType(type);
            computer.setPetrolStationId(petrolStationId);
            computerService.updateComputer(computer);
        } else {
            Computer newComputer = new Computer(name, ipAddress, type, petrolStationId);
            computerService.createComputer(newComputer);
        }

        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

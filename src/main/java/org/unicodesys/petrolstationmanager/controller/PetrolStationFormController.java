package org.unicodesys.petrolstationmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.unicodesys.petrolstationmanager.model.PetrolStation;
import org.unicodesys.petrolstationmanager.service.PetrolStationService;

public class PetrolStationFormController {

    @FXML
    private TextField stationNumberField;
    @FXML
    private TextField stationNameField;
    @FXML
    private TextField stationAddressField;
    @FXML
    private TextField stationCountryField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final PetrolStationService petrolStationService = new PetrolStationService();
    private PetrolStation petrolStation;

    public void setUpdateMode(PetrolStation petrolStation) {
        this.petrolStation = petrolStation;
        if (petrolStation != null) {
            stationNumberField.setText(String.valueOf(petrolStation.getNumber()));
            stationNameField.setText(petrolStation.getName());
            stationAddressField.setText(petrolStation.getAddress());
            stationCountryField.setText(petrolStation.getCountry());
        }
    }

    @FXML
    private void handleSave() {
        String stationNumber = stationNumberField.getText();
        String stationName = stationNameField.getText();
        String stationAddress = stationAddressField.getText();
        String stationCountry = stationCountryField.getText();

        if (stationNumber.isEmpty() || stationName.isEmpty() || stationAddress.isEmpty() || stationCountry.isEmpty()) {
            System.out.println("All fields must be filled.");
            return;
        }

        if (petrolStation == null) {
            petrolStation = new PetrolStation(stationNumber, stationName, stationAddress, stationCountry);
            petrolStationService.createPetrolStation(petrolStation);
        } else {
            petrolStation.setNumber(stationNumber);
            petrolStation.setName(stationName);
            petrolStation.setAddress(stationAddress);
            petrolStation.setCountry(stationCountry);
            petrolStationService.updatePetrolStation(petrolStation);
        }

        closeForm();
    }

    @FXML
    private void handleCancel() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}

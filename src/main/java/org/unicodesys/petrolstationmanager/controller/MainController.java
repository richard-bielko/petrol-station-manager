package org.unicodesys.petrolstationmanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.unicodesys.petrolstationmanager.model.Computer;
import org.unicodesys.petrolstationmanager.model.PetrolStation;
import org.unicodesys.petrolstationmanager.service.ComputerService;
import org.unicodesys.petrolstationmanager.service.PetrolStationService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML
    private ListView<PetrolStation> petrolStationList;
    @FXML
    private ListView<Computer> computerList;
    @FXML
    private Button createStationButton , updateStationButton, deleteStationButton;
    @FXML
    private Button createComputerButton, updateComputerButton, deleteComputerButton;

    private final PetrolStationService petrolStationService = new PetrolStationService();
    private final ComputerService computerService = new ComputerService();

    private final ObservableList<PetrolStation> petrolStations = FXCollections.observableArrayList();
    private final ObservableList<Computer> computers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loadPetrolStations();
        petrolStationList.setItems(petrolStations);
        computerList.setItems(computers);

        petrolStationList.setOnMouseClicked(this::onPetrolStationSelected);

        // Set button actions
        createStationButton.setOnAction(event -> {
            try {
                openPetrolStationForm();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createComputerButton.setOnAction(event -> {
            try {
                openComputerForm();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        updateComputerButton.setOnAction(event -> {
            Computer selectedComputer = computerList.getSelectionModel().getSelectedItem();
            if (selectedComputer != null) {
                openComputerUpdateForm(selectedComputer);
            } else {
                showAlert("Selection Error", "Please select a computer to update.");
            }
        });

        deleteComputerButton.setOnAction(event -> {
            Computer selectedComputer = computerList.getSelectionModel().getSelectedItem();
            if (selectedComputer != null) {
                deleteComputer(selectedComputer);
            } else {
                showAlert("Selection Error", "Please select a computer to delete.");
            }
        });

        updateStationButton.setOnAction(event -> {
            PetrolStation selectedStation = petrolStationList.getSelectionModel().getSelectedItem();
            if (selectedStation != null) {
                openPetrolStationUpdateForm(selectedStation);
            } else {
                showAlert("Selection Error", "Please select a petrol station to update.");
            }
        });

        deleteStationButton.setOnAction(event -> {
            PetrolStation selectedStation = petrolStationList.getSelectionModel().getSelectedItem();
            if (selectedStation != null) {
                deletePetrolStation(selectedStation);
            } else {
                showAlert("Selection Error", "Please select a petrol station to delete.");
            }
        });
    }

    private void loadPetrolStations() {
        petrolStations.setAll(petrolStationService.listAllPetrolStations());
    }

    private void onPetrolStationSelected(MouseEvent event) {
        PetrolStation selectedStation = petrolStationList.getSelectionModel().getSelectedItem();
        if (selectedStation != null) {
            List<Computer> computerListForStation = computerService.listComputersForPetrolStation(selectedStation.getId());
            computers.setAll(computerListForStation);
        }
    }

    @FXML
    private void openPetrolStationForm() throws IOException {
        openForm("/petrol-station-form.fxml", "Manage Petrol Station");
        loadPetrolStations();
    }

    private void openPetrolStationUpdateForm(PetrolStation petrolStation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/petrol-station-form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Edit Petrol Station");
            stage.initModality(Modality.APPLICATION_MODAL);

            PetrolStationFormController controller = loader.getController();
            controller.setUpdateMode(petrolStation);

            stage.showAndWait();
            loadPetrolStations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePetrolStation(PetrolStation petrolStation) {
        boolean confirm = showConfirmation("Delete Petrol Station", "Are you sure you want to delete this petrol station?");
        if (confirm) {
            petrolStationService.deletePetrolStation(petrolStation.getId());
            loadPetrolStations();
            computers.clear();
        }
    }

    @FXML
    private void openComputerForm() throws IOException {
        openForm("/computer-form.fxml", "Manage Computer");
        PetrolStation selectedStation = petrolStationList.getSelectionModel().getSelectedItem();
        if (selectedStation != null) {
            List<Computer> computerListForStation = computerService.listComputersForPetrolStation(selectedStation.getId());
            computers.setAll(computerListForStation);
        }
        refreshComputerList();
    }

    private void openComputerUpdateForm(Computer computer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/computer-form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Edit Computer");
            stage.initModality(Modality.APPLICATION_MODAL);

            ComputerFormController controller = loader.getController();
            controller.setUpdateMode(computer);

            stage.showAndWait();
            refreshComputerList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteComputer(Computer computer) {
        boolean confirm = showConfirmation("Delete Computer", "Are you sure you want to delete this computer?");
        if (confirm) {
            computerService.deleteComputer(computer.getId());
            refreshComputerList();
        }
    }

    private void openForm(String resourcePath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        if (loader.getLocation() == null) {
            throw new IOException("FXML file not found: " + resourcePath);
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void refreshComputerList() {
        PetrolStation selectedStation = petrolStationList.getSelectionModel().getSelectedItem();
        if (selectedStation != null) {
            computers.setAll(computerService.listComputersForPetrolStation(selectedStation.getId()));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
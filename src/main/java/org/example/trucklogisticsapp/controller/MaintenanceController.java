package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.MaintenanceRecord;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class MaintenanceController {

    @FXML private TableView<MaintenanceRecord> maintenanceTable;

    @FXML private TableColumn<MaintenanceRecord, String> colRecordId;
    @FXML private TableColumn<MaintenanceRecord, String> colTruckId;
    @FXML private TableColumn<MaintenanceRecord, String> colType;
    @FXML private TableColumn<MaintenanceRecord, LocalDate> colScheduled;
    @FXML private TableColumn<MaintenanceRecord, Double> colCost;
    @FXML private TableColumn<MaintenanceRecord, String> colStatus;

    private final ObservableList<MaintenanceRecord> records =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colRecordId.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getRecordId()));
        colTruckId.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getTruckId()));
        colType.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getType()));
        colScheduled.setCellValueFactory(e -> new javafx.beans.property.SimpleObjectProperty<>(e.getValue().getScheduledDate()));
        colCost.setCellValueFactory(e -> new javafx.beans.property.SimpleObjectProperty<>(e.getValue().getCost()));
        colStatus.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getStatus()));

        loadSampleData();
        maintenanceTable.setItems(records);
    }

    private void loadSampleData() {
        records.add(new MaintenanceRecord("M001", "TRK-001", LocalDate.now().plusDays(7), "Oil Change", 150, "Scheduled"));
        records.add(new MaintenanceRecord("M002", "TRK-002", LocalDate.now().plusDays(14), "Tire Rotation", 80, "Scheduled"));
    }

    // ------------------------------
    // ADD RECORD
    // ------------------------------
    @FXML
    private void onAddRecord() {
        try {
            URL fxml = getClass().getResource("/org/example/trucklogisticsapp/AddMaintenanceDialog.fxml");
            System.out.println("Add dialog URL = " + fxml);

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            AddMaintenanceController dialog = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add Maintenance Record");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            MaintenanceRecord newRec = dialog.getNewRecord();
            if (newRec != null) {
                records.add(newRec);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------
    // EDIT RECORD
    // ------------------------------
    @FXML
    private void onEditRecord() {
        MaintenanceRecord selected = maintenanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            URL fxml = getClass().getResource("/org/example/trucklogisticsapp/EditMaintenanceDialog.fxml");
            System.out.println("Edit dialog URL = " + fxml);

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            EditMaintenanceController dialog = loader.getController();
            dialog.setRecord(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Maintenance Record");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            maintenanceTable.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------
    // DELETE RECORD
    // ------------------------------
    @FXML
    private void onDeleteRecord() {
        MaintenanceRecord selected = maintenanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        records.remove(selected);
    }
}

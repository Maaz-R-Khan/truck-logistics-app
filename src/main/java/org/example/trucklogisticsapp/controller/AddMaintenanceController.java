package org.example.trucklogisticsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.example.trucklogisticsapp.model.MaintenanceRecord;

import java.time.LocalDate;

public class AddMaintenanceController {

    @FXML private TextField txtRecordId;
    @FXML private TextField txtTruckId;
    @FXML private TextField txtType;
    @FXML private DatePicker dpScheduled;
    @FXML private TextField txtCost;
    @FXML private TextField txtStatus;

    private MaintenanceRecord newRecord;

    public MaintenanceRecord getNewRecord() {
        return newRecord;
    }

    @FXML
    private void onSave() {
        newRecord = new MaintenanceRecord(
                txtRecordId.getText(),
                txtTruckId.getText(),
                dpScheduled.getValue(),
                txtType.getText(),
                Double.parseDouble(txtCost.getText()),
                txtStatus.getText()
        );

        ((Stage) txtRecordId.getScene().getWindow()).close();
    }

    public void onCancel(ActionEvent actionEvent) {
    }
}

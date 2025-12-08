package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.example.trucklogisticsapp.model.MaintenanceRecord;

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
                txtType.getText(),
                dpScheduled.getValue() != null ? dpScheduled.getValue().toString() : null,
                Double.parseDouble(txtCost.getText()),
                txtStatus.getText()
        );

        ((Stage) txtRecordId.getScene().getWindow()).close();
    }

    @FXML
    private void onCancel() {
        ((Stage) txtRecordId.getScene().getWindow()).close();
    }
}

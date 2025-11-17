package org.example.trucklogisticsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.example.trucklogisticsapp.model.MaintenanceRecord;

public class EditMaintenanceController {

    @FXML private TextField txtRecordId;
    @FXML private TextField txtTruckId;
    @FXML private TextField txtType;
    @FXML private DatePicker dpScheduled;
    @FXML private TextField txtCost;
    @FXML private TextField txtStatus;

    private MaintenanceRecord editable;

    public void setRecord(MaintenanceRecord r) {
        this.editable = r;

        txtRecordId.setText(r.getRecordId());
        txtTruckId.setText(r.getTruckId());
        txtType.setText(r.getType());
        dpScheduled.setValue(r.getScheduledDate());
        txtCost.setText(String.valueOf(r.getCost()));
        txtStatus.setText(r.getStatus());
    }

    @FXML
    private void onSave() {
        editable.setTruckId(txtTruckId.getText());
        editable.setType(txtType.getText());
        editable.setScheduledDate(dpScheduled.getValue());
        editable.setCost(Double.parseDouble(txtCost.getText()));
        editable.setStatus(txtStatus.getText());

        ((Stage) txtRecordId.getScene().getWindow()).close();
    }

    public void onUpdate(ActionEvent actionEvent) {
    }

    public void onCancel(ActionEvent actionEvent) {
    }
}

package org.example.trucklogisticsapp.controller;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Shipment;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class AddShipmentController {

    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private TextField weightField;
    @FXML private TextField valueField;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private DatePicker pickupDatePicker;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private TextField customerField;
    @FXML private TextArea notesArea;

    private Consumer<Shipment> onShipmentCreated;
    private String nextShipmentId = "SH-NEW";

    public void setOnShipmentCreated(Consumer<Shipment> onShipmentCreated) {
        this.onShipmentCreated = onShipmentCreated;
    }

    public void setNextShipmentId(String id) {
        this.nextShipmentId = id;
    }

    @FXML
    private void initialize() {
        priorityCombo.getItems().addAll("Low", "Medium", "High", "Urgent");
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onCreateShipment() {
        // Basic validation (keep it light, you can expand later)
        if (originField.getText().isBlank() || destinationField.getText().isBlank()) {
            showError("Origin and destination are required.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String pickupText = pickupDatePicker.getValue() != null
                ? pickupDatePicker.getValue().format(fmt)
                : "";
        String deliveryText = deliveryDatePicker.getValue() != null
                ? deliveryDatePicker.getValue().format(fmt)
                : "";

        String route = originField.getText() + "\n" + destinationField.getText();
        String deliveryInfo = deliveryText;

        Shipment shipment = new Shipment(
                nextShipmentId,
                route,
                customerField.getText().isBlank() ? "New Customer" : customerField.getText(),
                weightField.getText().isBlank() ? "0 lbs" : weightField.getText() + " lbs",
                valueField.getText().isBlank() ? "$ 0" : "$ " + valueField.getText(),
                priorityCombo.getValue() == null ? "Medium" : priorityCombo.getValue(),
                "Pending",
                "Unassigned",
                deliveryInfo.isBlank() ? "N/A" : deliveryInfo
        );

        if (onShipmentCreated != null) {
            onShipmentCreated.accept(shipment);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) originField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
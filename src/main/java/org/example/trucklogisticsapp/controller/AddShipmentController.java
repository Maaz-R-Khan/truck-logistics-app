package org.example.trucklogisticsapp.controller;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Shipment;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import javafx.scene.control.TextFormatter;

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

        valueField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null
        ));

    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onCreateShipment() {
        if (originField.getText().isBlank() || destinationField.getText().isBlank()) {
            showError("Origin and destination are required.");
            return;
        }

        var fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String pickupText = pickupDatePicker.getValue() != null
                ? pickupDatePicker.getValue().format(fmt) : "";
        String deliveryText = deliveryDatePicker.getValue() != null
                ? deliveryDatePicker.getValue().format(fmt) : "";

        String route = originField.getText() + "\n" + destinationField.getText();
        String customer = customerField.getText().isBlank() ? "New Customer" : customerField.getText();
        String priority = (priorityCombo.getValue() == null) ? "Medium" : priorityCombo.getValue();
        String deliveryInfo = deliveryText.isBlank() ? "N/A" : deliveryText;


        int value;
        try {
            value = valueField.getText().isBlank() ? 0 : Integer.parseInt(valueField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Value must be a whole number (no $ or commas).");
            return;
        }

        String weight = weightField.getText().isBlank() ? "0 lbs" : weightField.getText() + " lbs";

        Shipment shipment = new Shipment(
                nextShipmentId,
                route,
                customer,
                weight,
                value,
                priority,
                "Pending",
                "Unassigned",
                deliveryInfo
        );

        if (onShipmentCreated != null) onShipmentCreated.accept(shipment);
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
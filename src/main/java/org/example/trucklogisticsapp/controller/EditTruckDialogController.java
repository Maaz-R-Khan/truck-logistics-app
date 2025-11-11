package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Truck;

import java.time.LocalDate;

/**
 * THIS IS THE MISSING CONTROLLER THAT MAKES THE EDIT BUTTON WORK!
 * Controller for editing truck information
 */
public class EditTruckDialogController {

    @FXML private TextField txtVin;
    @FXML private TextField txtMake;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtCapacity;
    @FXML private TextField txtPlate;
    @FXML private CheckBox chkAvailable;
    @FXML private TextArea txtNotes;
    @FXML private DatePicker dateLastMaintenance;
    @FXML private ComboBox<Integer> cmbMaintenanceInterval;

    private Truck truck;

    @FXML
    public void initialize() {
        System.out.println("✅ EditTruckDialogController initialized");

        // Setup maintenance interval combo box
        cmbMaintenanceInterval.getItems().addAll(3, 6, 9, 12, 24);
        cmbMaintenanceInterval.setValue(6);
    }

    /**
     * Set the truck to be edited and populate all fields
     */
    public void setTruck(Truck truck) {
        this.truck = truck;
        System.out.println("📝 Loading truck data for editing: " + truck.getDisplayName());

        // Populate fields with current truck data
        txtVin.setText(truck.getVin());
        txtMake.setText(truck.getMake());
        txtModel.setText(truck.getModel());
        txtYear.setText(String.valueOf(truck.getYear()));
        txtCapacity.setText(String.valueOf(truck.getCapacityKg()));
        txtPlate.setText(truck.getPlateNumber());
        chkAvailable.setSelected(truck.isAvailable());
        txtNotes.setText(truck.getNotes() != null ? truck.getNotes() : "");

        if (truck.getLastMaintenanceDate() != null) {
            dateLastMaintenance.setValue(truck.getLastMaintenanceDate());
        }

        if (truck.getMaintenanceIntervalMonths() > 0) {
            cmbMaintenanceInterval.setValue(truck.getMaintenanceIntervalMonths());
        }

        System.out.println("✅ Truck data loaded successfully");
    }

    /**
     * Save changes and close the dialog
     */
    @FXML
    private void handleSave() {
        System.out.println("💾 Attempting to save changes...");

        if (!validateInput()) {
            System.out.println("⚠️ Validation failed");
            return;
        }

        try {
            // Update truck with new values
            truck.setVin(txtVin.getText().trim());
            truck.setMake(txtMake.getText().trim());
            truck.setModel(txtModel.getText().trim());
            truck.setYear(Integer.parseInt(txtYear.getText().trim()));
            truck.setCapacityKg(Integer.parseInt(txtCapacity.getText().trim()));
            truck.setPlateNumber(txtPlate.getText().trim());
            truck.setAvailable(chkAvailable.isSelected());
            truck.setNotes(txtNotes.getText());

            if (dateLastMaintenance.getValue() != null) {
                truck.setLastMaintenanceDate(dateLastMaintenance.getValue());
            }

            if (cmbMaintenanceInterval.getValue() != null) {
                truck.setMaintenanceIntervalMonths(cmbMaintenanceInterval.getValue());
            }

            System.out.println("✅ Truck updated successfully: " + truck.getDisplayName());

            // Close dialog
            Stage stage = (Stage) txtVin.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.err.println("❌ Number format error: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter valid numbers for year and capacity.\n\nYear: " + txtYear.getText() + "\nCapacity: " + txtCapacity.getText());
        } catch (Exception e) {
            System.err.println("❌ Error saving truck: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save truck: " + e.getMessage());
        }
    }

    /**
     * Cancel without saving and close the dialog
     */
    @FXML
    private void handleCancel() {
        System.out.println("❌ Edit cancelled by user");
        Stage stage = (Stage) txtVin.getScene().getWindow();
        stage.close();
    }

    /**
     * Validate all input fields
     */
    private boolean validateInput() {
        // VIN validation
        if (txtVin.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "VIN is required.");
            txtVin.requestFocus();
            return false;
        }

        // Make validation
        if (txtMake.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Make is required.");
            txtMake.requestFocus();
            return false;
        }

        // Model validation
        if (txtModel.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Model is required.");
            txtModel.requestFocus();
            return false;
        }

        // Year validation
        try {
            int year = Integer.parseInt(txtYear.getText().trim());
            if (year < 1900 || year > LocalDate.now().getYear() + 1) {
                showAlert(Alert.AlertType.ERROR, "Validation Error",
                        "Please enter a valid year between 1900 and " + (LocalDate.now().getYear() + 1) + ".");
                txtYear.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Year must be a number.\n\nYou entered: " + txtYear.getText());
            txtYear.requestFocus();
            return false;
        }

        // Capacity validation
        try {
            int capacity = Integer.parseInt(txtCapacity.getText().trim());
            if (capacity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error",
                        "Capacity must be greater than 0.\n\nYou entered: " + capacity);
                txtCapacity.requestFocus();
                return false;
            }
            if (capacity > 100000) {
                showAlert(Alert.AlertType.ERROR, "Validation Error",
                        "Capacity seems too high. Please check.\n\nYou entered: " + capacity + " kg");
                txtCapacity.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Capacity must be a number.\n\nYou entered: " + txtCapacity.getText());
            txtCapacity.requestFocus();
            return false;
        }

        System.out.println("✅ All validation passed");
        return true;
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
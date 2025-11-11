package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Truck;

/**
 * Add Truck Dialog Controller - Handles adding new trucks with validation
 */
public class AddTruckDialogController {

    @FXML private TextField txtVin;
    @FXML private TextField txtMake;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtCapacity;
    @FXML private TextField txtPlate;
    @FXML private TextField txtSource;  // NEW: Source/Dealer field
    @FXML private TextField txtMileage;
    @FXML private CheckBox chkAvailable;
    @FXML private CheckBox chkMaintenance;
    @FXML private TextArea txtNotes;
    @FXML private Label lblError;

    private Truck result;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Truck getResult() {
        return result;
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            try {
                // Create new truck
                Truck truck = new Truck(
                        txtVin.getText().trim(),
                        txtMake.getText().trim(),
                        txtModel.getText().trim(),
                        Integer.parseInt(txtYear.getText().trim()),
                        Integer.parseInt(txtCapacity.getText().trim())
                );

                // Set optional fields
                if (!txtPlate.getText().trim().isEmpty()) {
                    truck.setPlateNumber(txtPlate.getText().trim());
                }

                // Set source/dealer
                if (!txtSource.getText().trim().isEmpty()) {
                    truck.setSource(txtSource.getText().trim());
                }

                if (!txtMileage.getText().trim().isEmpty()) {
                    truck.setMileage(Double.parseDouble(txtMileage.getText().trim()));
                }

                truck.setAvailable(chkAvailable.isSelected());
                truck.setNeedsMaintenance(chkMaintenance.isSelected());

                if (!txtNotes.getText().trim().isEmpty()) {
                    truck.setNotes(txtNotes.getText().trim());
                }

                result = truck;
                System.out.println("✅ Truck created: " + truck.getDisplayName());
                dialogStage.close();

            } catch (NumberFormatException e) {
                showError("Please enter valid numbers for year, capacity, and mileage.");
            }
        }
    }

    @FXML
    private void handleCancel() {
        result = null;
        dialogStage.close();
        System.out.println("❌ Add truck cancelled");
    }

    /**
     * Validate all input fields
     */
    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        // VIN validation
        if (txtVin.getText().trim().isEmpty()) {
            errors.append("• VIN is required\n");
        } else if (txtVin.getText().trim().length() < 10) {
            errors.append("• VIN must be at least 10 characters\n");
        }

        // Make validation
        if (txtMake.getText().trim().isEmpty()) {
            errors.append("• Make is required\n");
        }

        // Model validation
        if (txtModel.getText().trim().isEmpty()) {
            errors.append("• Model is required\n");
        }

        // Year validation
        try {
            int year = Integer.parseInt(txtYear.getText().trim());
            if (year < 1990 || year > 2030) {
                errors.append("• Year must be between 1990 and 2030\n");
            }
        } catch (NumberFormatException e) {
            errors.append("• Year must be a valid number\n");
        }

        // Capacity validation
        try {
            double capacity = Double.parseDouble(txtCapacity.getText().trim());
            if (capacity <= 0) {
                errors.append("• Capacity must be greater than 0\n");
            }
        } catch (NumberFormatException e) {
            errors.append("• Capacity must be a valid number\n");
        }

        // Mileage validation (if provided)
        if (!txtMileage.getText().trim().isEmpty()) {
            try {
                double mileage = Double.parseDouble(txtMileage.getText().trim());
                if (mileage < 0) {
                    errors.append("• Mileage cannot be negative\n");
                }
            } catch (NumberFormatException e) {
                errors.append("• Mileage must be a valid number\n");
            }
        }

        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}
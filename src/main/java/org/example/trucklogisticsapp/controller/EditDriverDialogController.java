package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Driver;

import java.time.LocalDate;
import java.util.Optional;

public class EditDriverDialogController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private DatePicker dateOfBirth;
    @FXML private DatePicker dateHire;
    @FXML private TextField txtAddress;

    @FXML private TextField txtLicenseNumber;
    @FXML private ComboBox<String> cmbLicenseState;
    @FXML private ComboBox<String> cmbLicenseClass;
    @FXML private DatePicker dateLicenseExpiration;
    @FXML private DatePicker dateMedicalExpiration;

    @FXML private CheckBox chkHazmat;
    @FXML private CheckBox chkTankers;
    @FXML private CheckBox chkDoubles;
    @FXML private CheckBox chkAvailable;

    @FXML private TextArea txtNotes;
    @FXML private Button btnDelete;

    private Driver driver;
    private boolean deleted = false;

    @FXML
    public void initialize() {
        System.out.println("✅ EditDriverDialogController initialized");

        // Populate License States
        cmbLicenseState.getItems().addAll(
                "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
                "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho",
                "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana",
                "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
                "New Hampshire", "New Jersey", "New Mexico", "New York",
                "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
                "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
                "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
                "West Virginia", "Wisconsin", "Wyoming"
        );

        // Populate License Classes
        cmbLicenseClass.getItems().addAll("Class A", "Class B", "Class C");
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        System.out.println("📝 Loading driver data for editing: " + driver.getFullName());

        // Populate fields
        txtFirstName.setText(driver.getFirstName());
        txtLastName.setText(driver.getLastName());
        txtEmail.setText(driver.getEmail());
        txtPhone.setText(driver.getPhone());
        txtAddress.setText(driver.getAddress());
        txtLicenseNumber.setText(driver.getLicenseNumber());

        if (driver.getDateOfBirth() != null) {
            dateOfBirth.setValue(driver.getDateOfBirth());
        }

        if (driver.getHireDate() != null) {
            dateHire.setValue(driver.getHireDate());
        }

        cmbLicenseState.setValue(driver.getLicenseState());
        cmbLicenseClass.setValue(driver.getLicenseClass());

        if (driver.getLicenseExpiry() != null) {
            dateLicenseExpiration.setValue(driver.getLicenseExpiry());
        }

        if (driver.getMedicalCertExpiry() != null) {
            dateMedicalExpiration.setValue(driver.getMedicalCertExpiry());
        }

        chkHazmat.setSelected(driver.isHazmatEndorsement());
        chkTankers.setSelected(driver.isTankersEndorsement());
        chkDoubles.setSelected(driver.isDoublesEndorsement());
        chkAvailable.setSelected(driver.isAvailable());
        txtNotes.setText(driver.getNotes() != null ? driver.getNotes() : "");

        System.out.println("✅ Driver data loaded successfully");
    }

    public boolean wasDeleted() {
        return deleted;
    }

    @FXML
    private void handleSave() {
        System.out.println("💾 Attempting to save changes...");

        if (!validateInput()) {
            return;
        }

        try {
            driver.setFirstName(txtFirstName.getText().trim());
            driver.setLastName(txtLastName.getText().trim());
            driver.setEmail(txtEmail.getText().trim());
            driver.setPhone(txtPhone.getText().trim());
            driver.setAddress(txtAddress.getText().trim());
            driver.setLicenseNumber(txtLicenseNumber.getText().trim());
            driver.setLicenseState(cmbLicenseState.getValue());
            driver.setLicenseClass(cmbLicenseClass.getValue());

            if (dateOfBirth.getValue() != null) {
                driver.setDateOfBirth(dateOfBirth.getValue());
            }

            if (dateHire.getValue() != null) {
                driver.setHireDate(dateHire.getValue());
            }

            if (dateLicenseExpiration.getValue() != null) {
                driver.setLicenseExpiry(dateLicenseExpiration.getValue());
            }

            if (dateMedicalExpiration.getValue() != null) {
                driver.setMedicalCertExpiry(dateMedicalExpiration.getValue());
            }

            driver.setHazmatEndorsement(chkHazmat.isSelected());
            driver.setTankersEndorsement(chkTankers.isSelected());
            driver.setDoublesEndorsement(chkDoubles.isSelected());
            driver.setAvailable(chkAvailable.isSelected());
            driver.setNotes(txtNotes.getText());

            System.out.println("✅ Driver updated successfully: " + driver.getFullName());

            Stage stage = (Stage) txtFirstName.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            System.err.println("❌ Error saving driver: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save driver: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        System.out.println("🗑️ Delete button clicked for: " + driver.getFullName());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Driver");
        confirmation.setHeaderText("Delete " + driver.getFullName() + "?");
        confirmation.setContentText(
                "Are you sure you want to delete this driver?\n\n" +
                        "Name: " + driver.getFullName() + "\n" +
                        "License: " + driver.getLicenseNumber() + "\n\n" +
                        "This action cannot be undone!"
        );

        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(deleteButton, cancelButton);

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == deleteButton) {
            deleted = true;
            System.out.println("✅ Driver deletion confirmed by user");

            showAlert(Alert.AlertType.INFORMATION, "Driver Deleted",
                    "Driver " + driver.getFullName() + " has been deleted successfully.");

            Stage stage = (Stage) txtFirstName.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("❌ Driver deletion cancelled by user");
        }
    }

    @FXML
    private void handleCancel() {
        System.out.println("❌ Edit cancelled by user");
        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        if (txtFirstName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "First name is required.");
            return false;
        }
        if (txtLastName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Last name is required.");
            return false;
        }
        if (txtLicenseNumber.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "License number is required.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
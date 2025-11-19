package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Driver;

import java.time.LocalDate;

public class AddDriverDialogController {

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
    @FXML private Label lblError;

    private Driver result;
    private Stage dialogStage;

    @FXML
    public void initialize() {
        System.out.println("✅ AddDriverDialogController initialized");

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
        cmbLicenseClass.setValue("Class A");

        // Set default hire date to today
        dateHire.setValue(LocalDate.now());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Driver getResult() {
        return result;
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            try {
                Driver driver = new Driver(
                        txtFirstName.getText().trim(),
                        txtLastName.getText().trim(),
                        txtLicenseNumber.getText().trim(),
                        cmbLicenseState.getValue(),
                        txtPhone.getText().trim(),
                        txtEmail.getText().trim()
                );

                if (dateOfBirth.getValue() != null) {
                    driver.setDateOfBirth(dateOfBirth.getValue());
                }

                if (dateHire.getValue() != null) {
                    driver.setHireDate(dateHire.getValue());
                }

                if (!txtAddress.getText().trim().isEmpty()) {
                    driver.setAddress(txtAddress.getText().trim());
                }

                driver.setLicenseClass(cmbLicenseClass.getValue());

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
                driver.setStatus("Active");

                if (!txtNotes.getText().trim().isEmpty()) {
                    driver.setNotes(txtNotes.getText().trim());
                }

                result = driver;
                System.out.println("✅ Driver created: " + driver.getFullName());
                dialogStage.close();

            } catch (Exception e) {
                showError("Error creating driver: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        result = null;
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (txtFirstName.getText().trim().isEmpty()) {
            errors.append("• First name is required\n");
        }
        if (txtLastName.getText().trim().isEmpty()) {
            errors.append("• Last name is required\n");
        }
        if (txtPhone.getText().trim().isEmpty()) {
            errors.append("• Phone number is required\n");
        }
        if (txtLicenseNumber.getText().trim().isEmpty()) {
            errors.append("• License number is required\n");
        }
        if (cmbLicenseState.getValue() == null) {
            errors.append("• License state is required\n");
        }
        if (dateOfBirth.getValue() == null) {
            errors.append("• Date of birth is required\n");
        } else {
            int age = (int) java.time.temporal.ChronoUnit.YEARS.between(
                    dateOfBirth.getValue(), LocalDate.now());
            if (age < 21) {
                errors.append("• Driver must be at least 21 years old\n");
            }
        }
        if (dateHire.getValue() == null) {
            errors.append("• Hire date is required\n");
        }
        if (dateLicenseExpiration.getValue() == null) {
            errors.append("• License expiration date is required\n");
        }
        if (dateMedicalExpiration.getValue() == null) {
            errors.append("• Medical card expiration date is required\n");
        }

        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }
        return true;
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}
package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Driver;

import java.time.LocalDate;

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

        cmbLicenseClass.getItems().addAll("Class A", "Class B", "Class C");
    }

    public void setDriver(Driver driver) {
        this.driver = driver;

        txtFirstName.setText(driver.getFirstName());
        txtLastName.setText(driver.getLastName());
        txtEmail.setText(driver.getEmail());
        txtPhone.setText(driver.getPhone());
        txtAddress.setText(driver.getAddress());
        txtLicenseNumber.setText(driver.getLicenseNumber());

        // Convert String → LocalDate for DatePicker
        dateOfBirth.setValue(parseDate(driver.getDateOfBirth()));
        dateHire.setValue(parseDate(driver.getHireDate()));
        dateLicenseExpiration.setValue(parseDate(driver.getLicenseExpiry()));
        dateMedicalExpiration.setValue(parseDate(driver.getMedicalCertExpiry()));

        cmbLicenseState.setValue(driver.getLicenseState());
        cmbLicenseClass.setValue(driver.getLicenseClass());

        chkHazmat.setSelected(driver.isHazmatEndorsement());
        chkTankers.setSelected(driver.isTankersEndorsement());
        chkDoubles.setSelected(driver.isDoublesEndorsement());
        chkAvailable.setSelected(driver.isAvailable());

        txtNotes.setText(driver.getNotes() != null ? driver.getNotes() : "");

        System.out.println("✅ Driver loaded for editing");
    }

    // Helper to convert String → LocalDate
    private LocalDate parseDate(String value) {
        try {
            return (value == null || value.isBlank()) ? null : LocalDate.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    // Helper to convert LocalDate → String
    private String format(LocalDate d) {
        return d == null ? null : d.toString();
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) return;

        driver.setFirstName(txtFirstName.getText().trim());
        driver.setLastName(txtLastName.getText().trim());
        driver.setEmail(txtEmail.getText().trim());
        driver.setPhone(txtPhone.getText().trim());
        driver.setAddress(txtAddress.getText().trim());
        driver.setLicenseNumber(txtLicenseNumber.getText().trim());
        driver.setLicenseState(cmbLicenseState.getValue());
        driver.setLicenseClass(cmbLicenseClass.getValue());

        // Convert back LocalDate → String for Firestore
        driver.setDateOfBirth(format(dateOfBirth.getValue()));
        driver.setHireDate(format(dateHire.getValue()));
        driver.setLicenseExpiry(format(dateLicenseExpiration.getValue()));
        driver.setMedicalCertExpiry(format(dateMedicalExpiration.getValue()));

        driver.setHazmatEndorsement(chkHazmat.isSelected());
        driver.setTankersEndorsement(chkTankers.isSelected());
        driver.setDoublesEndorsement(chkDoubles.isSelected());
        driver.setAvailable(chkAvailable.isSelected());
        driver.setNotes(txtNotes.getText());

        System.out.println("💾 Driver saved: " + driver.getFullName());

        ((Stage) txtFirstName.getScene().getWindow()).close();
    }

    private boolean validateInput() {
        if (txtFirstName.getText().trim().isEmpty()) return showValidation("First name is required.");
        if (txtLastName.getText().trim().isEmpty()) return showValidation("Last name is required.");
        if (txtLicenseNumber.getText().trim().isEmpty()) return showValidation("License number is required.");
        return true;
    }

    private boolean showValidation(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
        return false;
    }

    @FXML
    private void handleCancel() {
        ((Stage) txtFirstName.getScene().getWindow()).close();
    }
}
package org.example.trucklogisticsapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.example.trucklogisticsapp.model.Driver;

import java.io.File;
import java.util.Optional;

/**
 * Driver Controller - Manages driver data and UI
 * Corresponds to DriverManagement.fxml
 */
public class DriverController {

    // Table and columns
    @FXML private TableView<Driver> driverTable;
    @FXML private TableColumn<Driver, String> colDriver;
    @FXML private TableColumn<Driver, String> colContact;
    @FXML private TableColumn<Driver, String> colLicense;
    @FXML private TableColumn<Driver, String> colStatus;
    @FXML private TableColumn<Driver, String> colRating;
    @FXML private TableColumn<Driver, Integer> colTrips;
    @FXML private TableColumn<Driver, String> colLocation;

    // Stats labels
    @FXML private Text lblTotalDrivers;
    @FXML private Text lblAvailableDrivers;
    @FXML private Text lblOnRoute;
    @FXML private Text lblAvgRating;

    // Form fields (for Add/Edit dialog - optional)
    @FXML private TextField txtName;
    @FXML private TextField txtLicense;
    @FXML private TextField txtExpiry;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private TextArea txtNotes;
    @FXML private CheckBox chkActive;
    @FXML private Label lblPhoto;
    @FXML private Label lblResume;

    // Buttons
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnSelectPhoto;
    @FXML private Button btnSelectResume;

    // Data
    private ObservableList<Driver> driverList;
    private Driver selectedDriver;
    private String selectedPhotoPath = "";
    private String selectedResumePath = "";

    // Sample data for display
    private String[] sampleStatuses = {"On Route", "Available", "On Route", "Off Duty"};
    private String[] sampleLocations = {"Phoenix, AZ", "Denver, CO", "Seattle, WA", "Dallas, TX"};
    private double[] sampleRatings = {4.8, 4.9, 4.6, 4.7};
    private int[] sampleTrips = {156, 203, 142, 89};

    /**
     * Initialize the controller after FXML is loaded
     */
    @FXML
    public void initialize() {
        // Initialize driver list
        driverList = FXCollections.observableArrayList();

        // Set up table columns
        setupTableColumns();

        // Bind table to data
        driverTable.setItems(driverList);

        // Add selection listener
        driverTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                }
        );

        // Load sample data
        loadSampleData();
        updateStats();
    }

    /**
     * Set up all table columns with custom cell factories
     */
    private void setupTableColumns() {
        // Driver column with avatar and name
        colDriver.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Driver driver = getTableView().getItems().get(getIndex());

                    // Create initials circle
                    String initials = getInitials(driver.getName());
                    Label initialsLabel = new Label(initials);
                    initialsLabel.setStyle(
                            "-fx-background-color: #E5E7EB; " +
                                    "-fx-text-fill: #4B5563; " +
                                    "-fx-background-radius: 50%; " +
                                    "-fx-min-width: 40px; " +
                                    "-fx-min-height: 40px; " +
                                    "-fx-alignment: center; " +
                                    "-fx-font-weight: 600; " +
                                    "-fx-font-size: 14px;"
                    );

                    // Create name and ID text
                    VBox nameBox = new VBox(2);
                    Text name = new Text(driver.getName());
                    name.setStyle("-fx-font-weight: 600; -fx-fill: #111827;");
                    Text id = new Text("DRV-" + driver.getId().substring(0, 3).toUpperCase());
                    id.setStyle("-fx-fill: #6B7280; -fx-font-size: 12px;");
                    nameBox.getChildren().addAll(name, id);

                    HBox container = new HBox(12, initialsLabel, nameBox);
                    container.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(container);
                }
            }
        });

        // Contact column with email and phone
        colContact.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Driver driver = getTableView().getItems().get(getIndex());
                    VBox contactBox = new VBox(2);

                    String email = driver.getName().toLowerCase().replace(" ", ".") + "@logitruck.com";
                    Text emailText = new Text(email);
                    emailText.setStyle("-fx-fill: #111827; -fx-font-size: 13px;");

                    String phone = driver.getPhone() != null && !driver.getPhone().isEmpty()
                            ? driver.getPhone() : "+1 (555) 123-4567";
                    Text phoneText = new Text("📞 " + phone);
                    phoneText.setStyle("-fx-fill: #6B7280; -fx-font-size: 12px;");

                    contactBox.getChildren().addAll(emailText, phoneText);
                    setGraphic(contactBox);
                }
            }
        });

        // License column with number and expiry
        colLicense.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Driver driver = getTableView().getItems().get(getIndex());
                    VBox licenseBox = new VBox(2);

                    Text licenseNum = new Text(driver.getLicenseNumber());
                    licenseNum.setStyle("-fx-fill: #111827; -fx-font-weight: 600;");

                    String expiry = driver.getLicenseExpiry() != null && !driver.getLicenseExpiry().isEmpty()
                            ? driver.getLicenseExpiry() : "2025-06-15";
                    Text expiryText = new Text("📅 Exp: " + expiry);
                    expiryText.setStyle("-fx-fill: #6B7280; -fx-font-size: 12px;");

                    licenseBox.getChildren().addAll(licenseNum, expiryText);
                    setGraphic(licenseBox);
                }
            }
        });

        // Status column with colored badge
        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(status);
                    badge.getStyleClass().add("status-badge");

                    switch (status) {
                        case "Available":
                            badge.getStyleClass().add("status-available");
                            break;
                        case "On Route":
                            badge.getStyleClass().add("status-on-route");
                            break;
                        case "Off Duty":
                            badge.getStyleClass().add("status-off-duty");
                            break;
                    }

                    setGraphic(badge);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        // Rating column with star
        colRating.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label ratingLabel = new Label("⭐ " + rating);
                    ratingLabel.setStyle("-fx-text-fill: #111827; -fx-font-weight: 600;");
                    setGraphic(ratingLabel);
                }
            }
        });

        // Location column with pin icon
        colLocation.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String location, boolean empty) {
                super.updateItem(location, empty);
                if (empty || location == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label locLabel = new Label("📍 " + location);
                    locLabel.setStyle("-fx-text-fill: #6B7280;");
                    setGraphic(locLabel);
                }
            }
        });

        // Set cell value factories
        colDriver.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colContact.setCellValueFactory(cellData ->
                new SimpleStringProperty("contact"));

        colLicense.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLicenseNumber()));

        colStatus.setCellValueFactory(cellData -> {
            int index = driverList.indexOf(cellData.getValue());
            return new SimpleStringProperty(sampleStatuses[index % sampleStatuses.length]);
        });

        colRating.setCellValueFactory(cellData -> {
            int index = driverList.indexOf(cellData.getValue());
            return new SimpleStringProperty(String.valueOf(sampleRatings[index % sampleRatings.length]));
        });

        colTrips.setCellValueFactory(cellData -> {
            int index = driverList.indexOf(cellData.getValue());
            return new javafx.beans.property.SimpleIntegerProperty(
                    sampleTrips[index % sampleTrips.length]).asObject();
        });

        colLocation.setCellValueFactory(cellData -> {
            int index = driverList.indexOf(cellData.getValue());
            return new SimpleStringProperty(sampleLocations[index % sampleLocations.length]);
        });
    }

    /**
     * Add a new driver
     */
    @FXML
    public void handleAdd() {
        // Check if form fields exist (optional dialog mode)
        if (txtName == null || txtLicense == null) {
            // Show dialog for adding driver
            showAddDriverDialog();
            return;
        }

        if (!validateInput()) {
            return;
        }

        // Create new driver
        Driver driver = new Driver(
                txtLicense.getText().trim(),
                txtName.getText().trim()
        );

        // Set optional fields
        driver.setLicenseExpiry(txtExpiry.getText().trim());
        driver.setPhone(txtPhone.getText().trim());
        driver.setAddress(txtAddress.getText().trim());
        driver.setBackgroundNotes(txtNotes.getText());
        driver.setActive(chkActive.isSelected());
        driver.setPhotoPath(selectedPhotoPath);
        driver.setResumePath(selectedResumePath);

        // Add to list
        driverList.add(driver);

        // Clear form
        clearFields();

        // Update stats
        updateStats();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Driver added successfully!");
    }

    /**
     * Update selected driver
     */
    @FXML
    public void handleUpdate() {
        if (selectedDriver == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a driver to update.");
            return;
        }

        if (!validateInput()) {
            return;
        }

        // Update driver properties
        selectedDriver.setName(txtName.getText().trim());
        selectedDriver.setLicenseNumber(txtLicense.getText().trim());
        selectedDriver.setLicenseExpiry(txtExpiry.getText().trim());
        selectedDriver.setPhone(txtPhone.getText().trim());
        selectedDriver.setAddress(txtAddress.getText().trim());
        selectedDriver.setBackgroundNotes(txtNotes.getText());
        selectedDriver.setActive(chkActive.isSelected());
        selectedDriver.setPhotoPath(selectedPhotoPath);
        selectedDriver.setResumePath(selectedResumePath);

        // Refresh table
        driverTable.refresh();

        clearFields();
        updateStats();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Driver updated successfully!");
    }

    /**
     * Delete selected driver
     */
    @FXML
    public void handleDelete() {
        if (selectedDriver == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a driver to delete.");
            return;
        }

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Driver");
        confirm.setContentText("Are you sure you want to delete " + selectedDriver.getName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            driverList.remove(selectedDriver);
            clearFields();
            updateStats();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Driver deleted successfully!");
        }
    }

    /**
     * Clear all input fields
     */
    @FXML
    public void handleClear() {
        clearFields();
    }

    /**
     * Select photo for driver
     */
    @FXML
    public void handleSelectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Driver Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(btnSelectPhoto.getScene().getWindow());
        if (file != null) {
            selectedPhotoPath = file.getAbsolutePath();
            lblPhoto.setText("Photo: " + file.getName());
        }
    }

    /**
     * Select resume for driver
     */
    @FXML
    public void handleSelectResume() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Driver Resume");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(btnSelectResume.getScene().getWindow());
        if (file != null) {
            selectedResumePath = file.getAbsolutePath();
            lblResume.setText("Resume: " + file.getName());
        }
    }

    /**
     * Populate form fields with selected driver data
     */
    private void populateFields(Driver driver) {
        selectedDriver = driver;

        if (txtName != null) {
            txtName.setText(driver.getName());
            txtLicense.setText(driver.getLicenseNumber());
            txtExpiry.setText(driver.getLicenseExpiry());
            txtPhone.setText(driver.getPhone());
            txtAddress.setText(driver.getAddress());
            txtNotes.setText(driver.getBackgroundNotes());
            chkActive.setSelected(driver.isActive());

            selectedPhotoPath = driver.getPhotoPath() != null ? driver.getPhotoPath() : "";
            selectedResumePath = driver.getResumePath() != null ? driver.getResumePath() : "";

            if (!selectedPhotoPath.isEmpty()) {
                lblPhoto.setText("Photo: " + new File(selectedPhotoPath).getName());
            } else {
                lblPhoto.setText("No photo selected");
            }

            if (!selectedResumePath.isEmpty()) {
                lblResume.setText("Resume: " + new File(selectedResumePath).getName());
            } else {
                lblResume.setText("No resume selected");
            }
        }
    }

    /**
     * Clear all form fields
     */
    private void clearFields() {
        selectedDriver = null;

        if (txtName != null) {
            txtName.clear();
            txtLicense.clear();
            txtExpiry.clear();
            txtPhone.clear();
            txtAddress.clear();
            txtNotes.clear();
            chkActive.setSelected(true);
            selectedPhotoPath = "";
            selectedResumePath = "";
            lblPhoto.setText("No photo selected");
            lblResume.setText("No resume selected");
        }

        driverTable.getSelectionModel().clearSelection();
    }

    /**
     * Validate input fields
     */
    private boolean validateInput() {
        if (txtName == null) return true; // Skip validation if form not present

        if (txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name is required.");
            return false;
        }
        if (txtLicense.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "License number is required.");
            return false;
        }

        return true;
    }

    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show dialog for adding a new driver
     */
    private void showAddDriverDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Driver");
        alert.setHeaderText("Add New Driver");
        alert.setContentText("This would open a dialog to add a new driver.\n\n" +
                "Create a separate FXML dialog for the add/edit form.");
        alert.showAndWait();
    }

    /**
     * Get initials from name
     */
    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    /**
     * Load sample data matching your Figma design
     */
    private void loadSampleData() {
        String[] names = {"John Smith", "Sarah Johnson", "Mike Wilson", "Lisa Brown"};
        String[] licenses = {"DL123456789", "DL987654321", "DL456789123", "DL789123456"};
        String[] phones = {"+1 (555) 123-4567", "+1 (555) 234-5678", "+1 (555) 345-6789", "+1 (555) 456-7890"};
        String[] expiries = {"2025-06-15", "2025-12-20", "2024-08-30", "2025-03-10"};

        for (int i = 0; i < names.length; i++) {
            Driver driver = new Driver(licenses[i], names[i]);
            driver.setPhone(phones[i]);
            driver.setLicenseExpiry(expiries[i]);
            driver.setActive(!sampleStatuses[i].equals("Off Duty"));
            driverList.add(driver);
        }
    }

    /**
     * Update statistics display
     */
    private void updateStats() {
        if (lblTotalDrivers != null) {
            lblTotalDrivers.setText(String.valueOf(driverList.size()));

            long available = 0;
            long onRoute = 0;

            for (int i = 0; i < driverList.size(); i++) {
                String status = sampleStatuses[i % sampleStatuses.length];
                if (status.equals("Available")) available++;
                if (status.equals("On Route")) onRoute++;
            }

            lblAvailableDrivers.setText(String.valueOf(available));
            lblOnRoute.setText(String.valueOf(onRoute));
            lblAvgRating.setText("4.8");
        }
    }

    // Getters for accessing data from other controllers
    public ObservableList<Driver> getDriverList() {
        return driverList;
    }

    public Driver getSelectedDriver() {
        return driverTable.getSelectionModel().getSelectedItem();
    }
}
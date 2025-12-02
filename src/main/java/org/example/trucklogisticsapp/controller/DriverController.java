package org.example.trucklogisticsapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Driver;

import java.time.LocalDate;
import java.util.Optional;

public class DriverController {

    @FXML private Text lblTotalDrivers;
    @FXML private Text lblAvailableDrivers;
    @FXML private Text lblAvgRating;

    @FXML private TableView<Driver> driverTable;
    @FXML private TableColumn<Driver, String> colDriverId;
    @FXML private TableColumn<Driver, String> colName;
    @FXML private TableColumn<Driver, String> colLicense;
    @FXML private TableColumn<Driver, String> colPhone;
    @FXML private TableColumn<Driver, String> colStatus;
    @FXML private TableColumn<Driver, String> colCompliance;
    @FXML private TableColumn<Driver, String> colEndorsements;
    @FXML private TableColumn<Driver, Void> colActions;

    private ObservableList<Driver> driverList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("✅ DriverController initialized");
        setupTable();
        loadSampleData();
        updateStats();
    }

    private void setupTable() {
        // Setup columns
        colDriverId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Name column - show full name
        colName.setCellValueFactory(data -> {
            Driver driver = data.getValue();
            return new javafx.beans.property.SimpleStringProperty(driver.getFullName());
        });

        colLicense.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Status column with badges
        colStatus.setCellValueFactory(data -> {
            Driver driver = data.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    driver.isAvailable() ? "Available" : "Assigned"
            );
        });
        colStatus.setCellFactory(col -> new TableCell<Driver, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    if (item.equals("Available")) {
                        badge.getStyleClass().add("status-badge-green");
                    } else {
                        badge.getStyleClass().add("status-badge-blue");
                    }
                    setGraphic(badge);
                }
            }
        });

        // Compliance column with color coding
        colCompliance.setCellValueFactory(data -> {
            Driver driver = data.getValue();
            return new javafx.beans.property.SimpleStringProperty(driver.getComplianceStatus());
        });
        colCompliance.setCellFactory(col -> new TableCell<Driver, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    if (item.equals("Compliant")) {
                        badge.getStyleClass().add("status-badge-green");
                    } else if (item.contains("Expiring")) {
                        badge.getStyleClass().add("status-badge-yellow");
                    } else {
                        badge.getStyleClass().add("status-badge-red");
                    }
                    setGraphic(badge);
                }
            }
        });

        // Endorsements column
        colEndorsements.setCellValueFactory(data -> {
            Driver driver = data.getValue();
            return new javafx.beans.property.SimpleStringProperty(driver.getEndorsements());
        });

        // Actions column with styled buttons
        colActions.setCellFactory(col -> new TableCell<Driver, Void>() {
            private final Button btnEdit = createStyledButton("✏️", "#007bff", "Edit driver details");
            private final Button btnView = createStyledButton("👁️", "#17a2b8", "View full details");
            private final Button btnAssign = createStyledButton("🚛", "#28a745", "Assign to truck");

            {
                btnEdit.setOnAction(e -> {
                    Driver driver = getTableView().getItems().get(getIndex());
                    handleEditDriver(driver);
                });

                btnView.setOnAction(e -> {
                    Driver driver = getTableView().getItems().get(getIndex());
                    handleViewDriver(driver);
                });

                btnAssign.setOnAction(e -> {
                    Driver driver = getTableView().getItems().get(getIndex());
                    handleAssignDriver(driver);
                });
            }

            private Button createStyledButton(String icon, String color, String tooltipText) {
                Button btn = new Button(icon);
                btn.setStyle(
                        "-fx-background-color: " + color + "; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 14px; " +
                                "-fx-min-width: 32px; " +
                                "-fx-min-height: 32px; " +
                                "-fx-background-radius: 5; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 4;"
                );

                btn.setOnMouseEntered(e -> btn.setStyle(
                        btn.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
                ));
                btn.setOnMouseExited(e -> btn.setStyle(
                        btn.getStyle().replace("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);", "")
                ));

                btn.setTooltip(new Tooltip(tooltipText));
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(6, btnEdit, btnView, btnAssign);
                    buttons.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(buttons);
                }
            }
        });

        driverTable.setItems(driverList);
    }

    private void loadSampleData() {
        // Sample driver data
        Driver driver1 = new Driver("John", "Smith", "D1234567", "California", "555-123-4567", "john.smith@email.com");
        driver1.setId("DRV-001");
        driver1.setDateOfBirth(LocalDate.of(1985, 5, 15));
        driver1.setHireDate(LocalDate.of(2020, 6, 1));
        driver1.setLicenseClass("Class A");
        driver1.setLicenseExpiry(LocalDate.of(2027, 12, 31));
        driver1.setMedicalCertExpiry(LocalDate.of(2025, 8, 15));
        driver1.setHazmatEndorsement(true);
        driver1.setTankersEndorsement(true);
        driver1.setAvailable(true);
        driver1.setRating(4.8);
        driver1.setTotalTrips(245);
        driver1.setTotalMiles(125000);

        Driver driver2 = new Driver("Jane", "Doe", "D7654321", "Texas", "555-987-6543", "jane.doe@email.com");
        driver2.setId("DRV-002");
        driver2.setDateOfBirth(LocalDate.of(1990, 8, 20));
        driver2.setHireDate(LocalDate.of(2021, 3, 15));
        driver2.setLicenseClass("Class A");
        driver2.setLicenseExpiry(LocalDate.of(2028, 6, 30));
        driver2.setMedicalCertExpiry(LocalDate.of(2026, 2, 28));
        driver2.setHazmatEndorsement(true);
        driver2.setAvailable(false);
        driver2.setAssignedTruckId("TRK-001");
        driver2.setRating(4.9);
        driver2.setTotalTrips(189);
        driver2.setTotalMiles(98000);

        Driver driver3 = new Driver("Bob", "Johnson", "D9876543", "Florida", "555-456-7890", "bob.johnson@email.com");
        driver3.setId("DRV-003");
        driver3.setDateOfBirth(LocalDate.of(1982, 3, 10));
        driver3.setHireDate(LocalDate.of(2019, 1, 10));
        driver3.setLicenseClass("Class A");
        driver3.setLicenseExpiry(LocalDate.of(2026, 9, 15));
        driver3.setMedicalCertExpiry(LocalDate.of(2025, 12, 1));
        driver3.setDoublesEndorsement(true);
        driver3.setAvailable(true);
        driver3.setRating(4.7);
        driver3.setTotalTrips(312);
        driver3.setTotalMiles(156000);

        Driver driver4 = new Driver("Alice", "Brown", "D4567890", "New York", "555-234-5678", "alice.brown@email.com");
        driver4.setId("DRV-004");
        driver4.setDateOfBirth(LocalDate.of(1988, 11, 25));
        driver4.setHireDate(LocalDate.of(2022, 7, 1));
        driver4.setLicenseClass("Class B");
        driver4.setLicenseExpiry(LocalDate.of(2027, 3, 31));
        driver4.setMedicalCertExpiry(LocalDate.of(2025, 9, 30));
        driver4.setAvailable(false);
        driver4.setAssignedTruckId("TRK-003");
        driver4.setRating(4.6);
        driver4.setTotalTrips(156);
        driver4.setTotalMiles(87000);

        driverList.addAll(driver1, driver2, driver3, driver4);
        System.out.println("📦 Loaded " + driverList.size() + " drivers");
    }

    private void updateStats() {
        lblTotalDrivers.setText(String.valueOf(driverList.size()));

        long available = driverList.stream().filter(Driver::isAvailable).count();
        lblAvailableDrivers.setText(String.valueOf(available));

        double avgRating = driverList.stream()
                .mapToDouble(Driver::getRating)
                .average()
                .orElse(0);
        lblAvgRating.setText(String.format("%.1f", avgRating));

        System.out.println("📊 Stats updated");
    }

    @FXML
    private void handleAddDriver() {
        System.out.println("➕ Add driver clicked");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/AddDriverDialog.fxml")
            );
            Parent root = loader.load();

            AddDriverDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Driver");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(driverTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            Driver newDriver = controller.getResult();
            if (newDriver != null) {
                driverList.add(newDriver);
                driverTable.refresh();
                updateStats();
                System.out.println("✅ Driver added successfully: " + newDriver.getFullName());
            }

        } catch (Exception e) {
            System.err.println("❌ Error opening Add Driver dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Add Driver dialog:\n" + e.getMessage());
        }
    }

    private void handleEditDriver(Driver driver) {
        System.out.println("✏️ Edit driver: " + driver.getId());
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/EditDriverDialog.fxml")
            );
            Parent root = loader.load();

            EditDriverDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Driver - " + driver.getFullName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(driverTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            controller.setDriver(driver);
            dialogStage.showAndWait();

            if (controller.wasDeleted()) {
                driverList.remove(driver);
                System.out.println("✅ Driver deleted from list");
            }

            driverTable.refresh();
            updateStats();
            System.out.println("✅ Driver edit dialog closed");

        } catch (Exception e) {
            System.err.println("❌ Error opening Edit Driver dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Edit Driver dialog:\n" + e.getMessage());
        }
    }

    private void handleViewDriver(Driver driver) {
        System.out.println("👁️ View driver: " + driver.getId());

        String details = String.format(
                "═══════════════════════════════\n" +
                        "       DRIVER DETAILS\n" +
                        "═══════════════════════════════\n\n" +
                        "👤 PERSONAL INFORMATION\n" +
                        "  • ID: %s\n" +
                        "  • Name: %s\n" +
                        "  • Email: %s\n" +
                        "  • Phone: %s\n" +
                        "  • Age: %d years\n\n" +
                        "🪪 LICENSE INFORMATION\n" +
                        "  • License Number: %s\n" +
                        "  • State: %s\n" +
                        "  • Class: %s\n" +
                        "  • Expiration: %s\n" +
                        "  • Medical Card Exp: %s\n" +
                        "  • Compliance: %s\n\n" +
                        "📋 ENDORSEMENTS\n" +
                        "  • %s\n\n" +
                        "📊 STATUS\n" +
                        "  • Available: %s\n" +
                        "  • Assigned Truck: %s\n" +
                        "  • Years of Service: %d\n\n" +
                        "⭐ PERFORMANCE\n" +
                        "  • Rating: %.1f / 5.0\n" +
                        "  • Total Trips: %d\n" +
                        "  • Total Miles: %,.0f\n\n" +
                        "📝 NOTES\n%s",
                driver.getId(),
                driver.getFullName(),
                driver.getEmail() != null && !driver.getEmail().isEmpty() ? driver.getEmail() : "Not Set",
                driver.getPhone(),
                driver.getAge(),
                driver.getLicenseNumber(),
                driver.getLicenseState(),
                driver.getLicenseClass(),
                driver.getLicenseExpiry() != null ? driver.getLicenseExpiry().toString() : "Not Set",
                driver.getMedicalCertExpiry() != null ? driver.getMedicalCertExpiry().toString() : "Not Set",
                driver.getComplianceStatus(),
                driver.getEndorsements(),
                driver.isAvailable() ? "Yes ✓" : "No (Assigned)",
                driver.getAssignedTruckId() != null ? driver.getAssignedTruckId() : "None",
                driver.getYearsOfService(),
                driver.getRating(),
                driver.getTotalTrips(),
                driver.getTotalMiles(),
                driver.getNotes() != null && !driver.getNotes().isEmpty() ? "  " + driver.getNotes() : "  No notes"
        );

        showAlert(Alert.AlertType.INFORMATION, "Driver Details", details);
    }

    private void handleAssignDriver(Driver driver) {
        System.out.println("🚛 Assign driver to truck: " + driver.getId());

        if (!driver.isAvailable()) {
            showAlert(Alert.AlertType.WARNING, "Driver Not Available",
                    "Driver " + driver.getFullName() + " is currently assigned to truck: " +
                            driver.getAssignedTruckId());
            return;
        }

        // This would open a truck selection dialog in a real app
        showAlert(Alert.AlertType.INFORMATION, "Assign Driver",
                "Truck assignment feature coming soon!\n\n" +
                        "This will allow you to assign " + driver.getFullName() + " to an available truck.");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
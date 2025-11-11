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
import org.example.trucklogisticsapp.model.MaintenanceResult;
import org.example.trucklogisticsapp.model.Truck;

import java.util.Optional;

public class TruckController {

    @FXML private Text lblTotalTrucks;
    @FXML private Text lblAvailableTrucks;
    @FXML private Text lblAvgCapacity;

    @FXML private TableView<Truck> truckTable;
    @FXML private TableColumn<Truck, String> colTruckId;
    @FXML private TableColumn<Truck, String> colLicensePlate;
    @FXML private TableColumn<Truck, String> colModel;
    @FXML private TableColumn<Truck, Integer> colCapacity;
    @FXML private TableColumn<Truck, String> colFuelMPG;
    @FXML private TableColumn<Truck, String> colStatus;
    @FXML private TableColumn<Truck, String> colCurrentDriver;
    @FXML private TableColumn<Truck, Double> colMileage;
    @FXML private TableColumn<Truck, Void> colActions;

    private ObservableList<Truck> truckList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("✅ TruckController initialized");
        setupTable();
        loadSampleData();
        updateStats();
    }

    private void setupTable() {
        // Setup columns
        colTruckId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLicensePlate.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));

        // Model column - combine make and model
        colModel.setCellValueFactory(data -> {
            Truck truck = data.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    truck.getMake() + " " + truck.getModel()
            );
        });

        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacityKg"));

        // Format capacity with " lbs"
        colCapacity.setCellFactory(col -> new TableCell<Truck, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d lbs", item));
                }
            }
        });

        // Fuel MPG column - sample data
        colFuelMPG.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty("⛽ 6.5");
        });

        // Status column with badges
        colStatus.setCellValueFactory(data -> {
            Truck truck = data.getValue();
            if (truck.isMaintenanceOverdue()) {
                return new javafx.beans.property.SimpleStringProperty("Maintenance");
            }
            return new javafx.beans.property.SimpleStringProperty(
                    truck.isAvailable() ? "Available" : "In Transit"
            );
        });
        colStatus.setCellFactory(col -> new TableCell<Truck, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    if (item.equals("Available")) {
                        badge.getStyleClass().add("status-badge-green");
                    } else if (item.equals("In Transit")) {
                        badge.getStyleClass().add("status-badge-blue");
                    } else if (item.equals("Maintenance")) {
                        badge.getStyleClass().add("status-badge-red");
                    } else {
                        badge.getStyleClass().add("status-badge-yellow");
                    }
                    setGraphic(badge);
                }
            }
        });

        // Current Driver column
        colCurrentDriver.setCellValueFactory(data -> {
            Truck truck = data.getValue();
            String driver = truck.isAvailable() ? "Unassigned" : "John Smith";
            return new javafx.beans.property.SimpleStringProperty(driver);
        });

        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        // Format mileage with commas
        colMileage.setCellFactory(col -> new TableCell<Truck, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        // Actions column with icon buttons - NOW WITH MAINTENANCE BUTTON!
        colActions.setCellFactory(col -> new TableCell<Truck, Void>() {
            private final Button btnEdit = new Button("✏️");
            private final Button btnView = new Button("👁️");
            private final Button btnMaintenance = new Button("⚙️");

            {
                // Style buttons
                String buttonStyle = "-fx-background-color: transparent; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 4;";

                btnEdit.setStyle(buttonStyle);
                btnView.setStyle(buttonStyle);
                btnMaintenance.setStyle(buttonStyle);

                btnEdit.setTooltip(new Tooltip("Edit truck"));
                btnView.setTooltip(new Tooltip("View details"));
                btnMaintenance.setTooltip(new Tooltip("Maintenance schedule"));

                btnEdit.setOnAction(e -> {
                    Truck truck = getTableView().getItems().get(getIndex());
                    handleEditTruck(truck);
                });

                btnView.setOnAction(e -> {
                    Truck truck = getTableView().getItems().get(getIndex());
                    handleViewTruck(truck);
                });

                btnMaintenance.setOnAction(e -> {
                    Truck truck = getTableView().getItems().get(getIndex());
                    handleMaintenanceTruck(truck);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(8, btnEdit, btnView, btnMaintenance);
                    buttons.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(buttons);
                }
            }
        });

        truckTable.setItems(truckList);
    }

    private void loadSampleData() {
        // Sample truck data matching the screenshot
        Truck truck1 = new Truck("1HGBH41JXMN109186", "Freightliner", "Cascadia", 2022, 26000);
        truck1.setPlateNumber("ABC-1234");
        truck1.setMileage(125000);
        truck1.setAvailable(false); // In Transit
        truck1.setId("TRK-001");

        Truck truck2 = new Truck("2HGFG12838H505034", "Volvo", "VNL", 2021, 24000);
        truck2.setPlateNumber("XYZ-5678");
        truck2.setMileage(98000);
        truck2.setAvailable(true);
        truck2.setId("TRK-002");

        Truck truck3 = new Truck("3FAHP0JA3CR277616", "Peterbilt", "579", 2023, 28000);
        truck3.setPlateNumber("DEF-9012");
        truck3.setMileage(156000);
        truck3.setAvailable(false); // Maintenance
        truck3.setId("TRK-003");

        Truck truck4 = new Truck("4HGBH41JXMN109187", "Kenworth", "T680", 2020, 25000);
        truck4.setPlateNumber("GHI-3456");
        truck4.setMileage(87000);
        truck4.setAvailable(false); // Loading
        truck4.setId("TRK-004");

        truckList.addAll(truck1, truck2, truck3, truck4);
        System.out.println("📦 Loaded " + truckList.size() + " trucks");
    }

    private void updateStats() {
        lblTotalTrucks.setText(String.valueOf(truckList.size()));

        long available = truckList.stream().filter(Truck::isAvailable).count();
        lblAvailableTrucks.setText(String.valueOf(available));

        double avgCapacity = truckList.stream()
                .mapToInt(Truck::getCapacityKg)
                .average()
                .orElse(0);
        lblAvgCapacity.setText(String.format("%,.0f", avgCapacity));

        System.out.println("📊 Stats updated");
    }

    /**
     * Handle Add Truck button - NOW ACTUALLY OPENS DIALOG!
     */
    @FXML
    private void handleAddTruck() {
        System.out.println("➕ Add truck clicked");
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/AddTruckDialog.fxml")
            );
            Parent root = loader.load();

            // Get the controller
            AddTruckDialogController controller = loader.getController();

            // Create and setup dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Truck");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(truckTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            // Pass the stage to controller
            controller.setDialogStage(dialogStage);

            // Show and wait
            dialogStage.showAndWait();

            // Get result
            Truck newTruck = controller.getResult();
            if (newTruck != null) {
                truckList.add(newTruck);
                truckTable.refresh();
                updateStats();
                System.out.println("✅ Truck added successfully: " + newTruck.getVin());
            }

        } catch (Exception e) {
            System.err.println("❌ Error opening Add Truck dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Add Truck dialog:\n" + e.getMessage());
        }
    }

    /**
     * Handle Edit Truck - NOW ACTUALLY OPENS DIALOG!
     */
    private void handleEditTruck(Truck truck) {
        System.out.println("✏️ Edit truck: " + truck.getId());
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/EditTruckDialog.fxml")
            );
            Parent root = loader.load();

            // Get the controller
            EditTruckDialogController controller = loader.getController();

            // Create and setup dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Truck - " + truck.getVin());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(truckTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            // Set the truck to edit
            controller.setTruck(truck);

            // Show and wait
            dialogStage.showAndWait();

            // Refresh the table
            truckTable.refresh();
            updateStats();
            System.out.println("✅ Truck edit dialog closed");

        } catch (Exception e) {
            System.err.println("❌ Error opening Edit Truck dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Edit Truck dialog:\n" + e.getMessage());
        }
    }

    /**
     * Handle Maintenance Setup - OPENS CUSTOM DIALOG!
     */
    private void handleMaintenanceTruck(Truck truck) {
        System.out.println("⚙️ Maintenance setup: " + truck.getId());
        try {
            // Use the custom MaintenanceDialog class
            MaintenanceDialog dialog = new MaintenanceDialog(truck);

            // Show and wait for result
            Optional<MaintenanceResult> result = dialog.showAndWait();

            if (result.isPresent()) {
                MaintenanceResult maintenance = result.get();

                // Update truck with new maintenance settings
                truck.setLastMaintenanceDate(maintenance.lastMaintenanceDate);
                truck.setMaintenanceIntervalMonths(maintenance.intervalMonths);

                // Refresh the table to show updated status
                truckTable.refresh();
                updateStats();

                System.out.println("✅ Maintenance schedule updated:");
                System.out.println("   Last: " + maintenance.lastMaintenanceDate);
                System.out.println("   Interval: " + maintenance.intervalMonths + " months");
                System.out.println("   Next due: " + truck.getNextMaintenanceDue());

                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Maintenance schedule updated!\n\n" +
                                "Last Maintenance: " + maintenance.lastMaintenanceDate + "\n" +
                                "Interval: " + maintenance.intervalMonths + " months\n" +
                                "Next Due: " + truck.getNextMaintenanceDue());
            }

        } catch (Exception e) {
            System.err.println("❌ Error opening Maintenance dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Maintenance dialog:\n" + e.getMessage());
        }
    }

    /**
     * Handle View Truck (placeholder for now)
     */
    private void handleViewTruck(Truck truck) {
        System.out.println("👁️ View truck: " + truck.getId());

        // Show truck details in alert
        String details = String.format(
                "Truck Details\n\n" +
                        "ID: %s\n" +
                        "VIN: %s\n" +
                        "Make/Model: %s %s\n" +
                        "Year: %d\n" +
                        "Capacity: %,d lbs\n" +
                        "Plate: %s\n" +
                        "Mileage: %,.0f\n" +
                        "Status: %s\n" +
                        "Maintenance: %s\n" +
                        "Notes: %s",
                truck.getId(),
                truck.getVin(),
                truck.getMake(),
                truck.getModel(),
                truck.getYear(),
                truck.getCapacityKg(),
                truck.getPlateNumber(),
                truck.getMileage(),
                truck.isAvailable() ? "Available" : "In Use",
                truck.getMaintenanceStatus(),
                truck.getNotes() != null ? truck.getNotes() : "None"
        );

        showAlert(Alert.AlertType.INFORMATION, "Truck Details", details);
    }

    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
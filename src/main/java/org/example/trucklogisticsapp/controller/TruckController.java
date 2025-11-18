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
    @FXML private TableColumn<Truck, String> colSource;  // NEW: Source column
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

        // NEW: Source column
        colSource.setCellValueFactory(new PropertyValueFactory<>("source"));
        colSource.setCellFactory(col -> new TableCell<Truck, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setText("-");
                    setStyle("-fx-text-fill: #9ca3af;");
                } else {
                    setText(item);
                    setStyle("");
                }
            }
        });

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

        // Actions column with STYLED ICON BUTTONS
        colActions.setCellFactory(col -> new TableCell<Truck, Void>() {
            private final Button btnEdit = createStyledButton("✏️", "#007bff", "Edit truck details");
            private final Button btnView = createStyledButton("👁️", "#17a2b8", "View full details");
            private final Button btnMaintenance = createStyledButton("⚙️", "#6f42c1", "Schedule maintenance");

            {
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

                // Hover effect
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
                    HBox buttons = new HBox(6, btnEdit, btnView, btnMaintenance);
                    buttons.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(buttons);
                }
            }
        });

        truckTable.setItems(truckList);
    }

    private void loadSampleData() {
        // Sample truck data with sources
        Truck truck1 = new Truck("1HGBH41JXMN109186", "Freightliner", "Cascadia", 2022, 26000);
        truck1.setPlateNumber("ABC-1234");
        truck1.setMileage(125000);
        truck1.setAvailable(false);
        truck1.setId("TRK-001");
        truck1.setSource("Mercedes Dealer");

        Truck truck2 = new Truck("2HGFG12838H505034", "Volvo", "VNL", 2021, 24000);
        truck2.setPlateNumber("XYZ-5678");
        truck2.setMileage(98000);
        truck2.setAvailable(true);
        truck2.setId("TRK-002");
        truck2.setSource("Auction");

        Truck truck3 = new Truck("3FAHP0JA3CR277616", "Peterbilt", "579", 2023, 28000);
        truck3.setPlateNumber("DEF-9012");
        truck3.setMileage(156000);
        truck3.setAvailable(false);
        truck3.setId("TRK-003");
        truck3.setSource("Trade-In");

        Truck truck4 = new Truck("4HGBH41JXMN109187", "Kenworth", "T680", 2020, 25000);
        truck4.setPlateNumber("GHI-3456");
        truck4.setMileage(87000);
        truck4.setAvailable(false);
        truck4.setId("TRK-004");
        truck4.setSource("Direct Purchase");

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

    @FXML
    private void handleAddTruck() {
        System.out.println("➕ Add truck clicked");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/AddCargoItemDialog")
            );
            Parent root = loader.load();

            AddTruckDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Truck");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(truckTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

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

    private void handleEditTruck(Truck truck) {
        System.out.println("✏️ Edit truck: " + truck.getId());
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/EditTruckDialog.fxml")
            );
            Parent root = loader.load();

            EditTruckDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Truck - " + truck.getVin());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(truckTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            controller.setTruck(truck);
            dialogStage.showAndWait();

            // Check if truck was deleted
            if (controller.wasDeleted()) {
                truckList.remove(truck);
                System.out.println("✅ Truck deleted from list");
            }

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

    private void handleMaintenanceTruck(Truck truck) {
        System.out.println("⚙️ Maintenance setup: " + truck.getId());
        try {
            MaintenanceDialog dialog = new MaintenanceDialog(truck);
            Optional<MaintenanceResult> result = dialog.showAndWait();

            if (result.isPresent()) {
                MaintenanceResult maintenance = result.get();
                truck.setLastMaintenanceDate(maintenance.lastMaintenanceDate);
                truck.setMaintenanceIntervalMonths(maintenance.intervalMonths);

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

    private void handleViewTruck(Truck truck) {
        System.out.println("👁️ View truck: " + truck.getId());

        String details = String.format(
                "═══════════════════════════════\n" +
                        "        TRUCK DETAILS\n" +
                        "═══════════════════════════════\n\n" +
                        "🚛 IDENTIFICATION\n" +
                        "  • ID: %s\n" +
                        "  • VIN: %s\n" +
                        "  • License Plate: %s\n\n" +
                        "📋 SPECIFICATIONS\n" +
                        "  • Make/Model: %s %s\n" +
                        "  • Year: %d\n" +
                        "  • Capacity: %,d lbs\n" +
                        "  • Current Mileage: %,.0f\n\n" +
                        "📍 ACQUISITION\n" +
                        "  • Source: %s\n\n" +
                        "📊 STATUS\n" +
                        "  • Available: %s\n" +
                        "  • Maintenance: %s\n\n" +
                        "📝 NOTES\n%s",
                truck.getId(),
                truck.getVin(),
                truck.getPlateNumber() != null && !truck.getPlateNumber().isEmpty() ? truck.getPlateNumber() : "Not Set",
                truck.getMake(),
                truck.getModel(),
                truck.getYear(),
                truck.getCapacityKg(),
                truck.getMileage(),
                truck.getSource() != null && !truck.getSource().isEmpty() ? truck.getSource() : "Not Specified",
                truck.isAvailable() ? "Yes ✓" : "No (In Use)",
                truck.getMaintenanceStatus(),
                truck.getNotes() != null && !truck.getNotes().isEmpty() ? "  " + truck.getNotes() : "  No notes"
        );

        showAlert(Alert.AlertType.INFORMATION, "Truck Details", details);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
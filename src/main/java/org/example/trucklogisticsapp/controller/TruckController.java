package org.example.trucklogisticsapp.controller;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.internal.NonNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.example.trucklogisticsapp.controller.FirestoreContext;

import java.util.HashMap;
import java.util.Map;
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
        // Load trucks from Firestore on a background thread
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                // Query all documents in the "trucks" collection
                ApiFuture<QuerySnapshot> future = db.collection("trucks").get();
                QuerySnapshot snapshot = future.get();

                // Temporary list to hold loaded trucks
                ObservableList<Truck> loadedTrucks = FXCollections.observableArrayList();

                for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                    // Map Firestore document to Truck POJO
                    Truck truck = doc.toObject(Truck.class);

                    // Ensure the Truck has its Firestore document ID set
                    if (truck.getId() == null || truck.getId().isEmpty()) {
                        truck.setId(doc.getId());
                    }

                    loadedTrucks.add(truck);
                }

                // Update UI on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> {
                    truckList.clear();
                    truckList.addAll(loadedTrucks);
                    updateStats();
                    System.out.println("📦 Loaded " + loadedTrucks.size() + " trucks from Firestore");
                });

            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not load trucks from Firestore:\n" + e.getMessage())
                );
            }
        }).start();
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
                    getClass().getResource("/org/example/trucklogisticsapp/AddTruckDialog.fxml")
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
                // 1) Update UI list
                truckList.add(newTruck);
                truckTable.refresh();
                updateStats();
                System.out.println("✅ Truck added locally: " + newTruck.getVin());

                // 2) Save to Firestore (in background)
                saveTruckToFirestore(newTruck);
            }

        } catch (Exception e) {
            System.err.println("❌ Error opening Add Truck dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Add Truck dialog:\n" + e.getMessage());
        }
    }

    private void saveTruckToFirestore(Truck truck) {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                // Use truck ID as the document ID (or generate one if null)
                String docId = (truck.getId() != null && !truck.getId().isEmpty())
                        ? truck.getId()
                        : db.collection("trucks").document().getId();

                if (truck.getId() == null || truck.getId().isEmpty()) {
                    truck.setId(docId); // keep UI in sync with Firestore ID
                }

                ApiFuture<WriteResult> future =
                        db.collection("trucks").document(docId).set(truck);

                WriteResult result = future.get();
                System.out.println("✅ Truck saved to Firestore at: " + result.getUpdateTime());

            } catch (Exception ex) {
                System.err.println("❌ Failed to save truck to Firestore");
                ex.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not save truck to Firestore:\n" + ex.getMessage())
                );
            }
        }).start();
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
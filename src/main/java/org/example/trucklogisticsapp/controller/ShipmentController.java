package org.example.trucklogisticsapp.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Shipment;

import java.io.IOException;

public class ShipmentController {

    // ============ FXML BINDINGS ============

    @FXML private TableView<Shipment> shipmentTable;

    @FXML private TableColumn<Shipment, String> idColumn;
    @FXML private TableColumn<Shipment, String> routeColumn;
    @FXML private TableColumn<Shipment, String> customerColumn;
    @FXML private TableColumn<Shipment, String> weightColumn;
    @FXML private TableColumn<Shipment, String> valueColumn;
    @FXML private TableColumn<Shipment, String> priorityColumn;
    @FXML private TableColumn<Shipment, String> statusColumn;
    @FXML private TableColumn<Shipment, String> assignmentColumn;
    @FXML private TableColumn<Shipment, String> deliveryColumn;
    @FXML private TableColumn<Shipment, Void>   actionsColumn;

    // ============ STATE ============

    private final ObservableList<Shipment> shipments =
            FXCollections.observableArrayList();

    // ============ INITIALIZATION ============

    @FXML
    private void initialize() {
        setupTableColumns();
        configurePriorityColumn();
        configureStatusColumn();
        configureActionsColumn();

        shipmentTable.setItems(shipments);
        loadShipmentsFromFirestore();
    }

    private void setupTableColumns() {
        // Bind columns to properties
        idColumn.setCellValueFactory(data -> data.getValue().shipmentIdProperty());
        routeColumn.setCellValueFactory(data -> data.getValue().routeProperty());
        customerColumn.setCellValueFactory(data -> data.getValue().customerProperty());
        weightColumn.setCellValueFactory(data -> data.getValue().weightProperty());
        valueColumn.setCellValueFactory(data -> data.getValue().valueProperty());
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        assignmentColumn.setCellValueFactory(data -> data.getValue().assignmentProperty());
        deliveryColumn.setCellValueFactory(data -> data.getValue().deliveryProperty());

        // Make some text wrap (route, assignment, delivery)
        wrapColumnText(routeColumn);
        wrapColumnText(assignmentColumn);
        wrapColumnText(deliveryColumn);
    }

    private void wrapColumnText(TableColumn<Shipment, String> column) {
        column.setCellFactory(col -> new TableCell<>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    label.setWrapText(true);
                    setGraphic(label);
                }
            }
        });
    }

    private void configurePriorityColumn() {
        priorityColumn.setCellFactory(col -> new TableCell<>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                label.setText(item);
                label.getStyleClass().setAll("badge");

                switch (item) {
                    case "High"   -> label.getStyleClass().add("badge-high");
                    case "Medium" -> label.getStyleClass().add("badge-medium");
                    case "Urgent" -> label.getStyleClass().add("badge-urgent");
                    default -> { /* leave base badge */ }
                }
                setGraphic(label);
            }
        });
    }

    private void configureStatusColumn() {
        statusColumn.setCellFactory(col -> new TableCell<>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                label.setText(item);
                label.getStyleClass().setAll("badge");

                switch (item) {
                    case "In Transit" -> label.getStyleClass().add("status-in-transit");
                    case "Delivered"  -> label.getStyleClass().add("status-delivered");
                    case "Assigned"   -> label.getStyleClass().add("status-assigned");
                    case "Pending"    -> label.getStyleClass().add("status-pending");
                    default -> { /* leave base badge */ }
                }
                setGraphic(label);
            }
        });
    }

    private void configureActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏");
            private final Button viewBtn = new Button("👁");
            private final HBox box = new HBox(8, editBtn, viewBtn);

            {
                box.getStyleClass().add("actions-box");
                editBtn.getStyleClass().add("ghost-btn");
                viewBtn.getStyleClass().add("ghost-btn");

                editBtn.setOnAction(e -> {
                    Shipment shipment = getTableView().getItems().get(getIndex());
                    // TODO: open edit dialog
                    System.out.println("Edit " + shipment.getShipmentId());
                });

                viewBtn.setOnAction(e -> {
                    Shipment shipment = getTableView().getItems().get(getIndex());
                    // TODO: open details view
                    System.out.println("View " + shipment.getShipmentId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
    }

    // ============ FIRESTORE: LOAD ============

    /**
     * Load shipments from Firestore ("shipments" collection)
     */
    private void loadShipmentsFromFirestore() {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                ApiFuture<QuerySnapshot> future =
                        db.collection("shipments").get();
                QuerySnapshot snapshot = future.get();

                ObservableList<Shipment> loadedShipments =
                        FXCollections.observableArrayList();

                for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                    Shipment shipment = doc.toObject(Shipment.class);

                    // Optional: ensure shipmentId matches Firestore document ID
                    if (shipment.getShipmentId() == null || shipment.getShipmentId().isEmpty()) {
                        shipment.setShipmentId(doc.getId());
                    }

                    loadedShipments.add(shipment);
                }

                Platform.runLater(() -> {
                    shipments.clear();
                    shipments.addAll(loadedShipments);
                    System.out.println("📦 Loaded " + loadedShipments.size() + " shipments from Firestore");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not load shipments from Firestore:\n" + e.getMessage())
                );
            }
        }).start();
    }

    // ============ FIRESTORE: SAVE ============

    /**
     * Save a single shipment to Firestore ("shipments" collection)
     */
    private void saveShipmentToFirestore(Shipment shipment) {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                // Use shipmentId as document ID, or generate one if missing
                String docId = (shipment.getShipmentId() != null && !shipment.getShipmentId().isEmpty())
                        ? shipment.getShipmentId()
                        : db.collection("shipments").document().getId();

                if (shipment.getShipmentId() == null || shipment.getShipmentId().isEmpty()) {
                    shipment.setShipmentId(docId); // keep UI model in sync
                }

                ApiFuture<WriteResult> future =
                        db.collection("shipments").document(docId).set(shipment);

                WriteResult result = future.get();
                System.out.println("✅ Shipment saved to Firestore at: " + result.getUpdateTime());

            } catch (Exception ex) {
                System.err.println("❌ Failed to save shipment to Firestore");
                ex.printStackTrace();
                Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not save shipment to Firestore:\n" + ex.getMessage())
                );
            }
        }).start();
    }

    // ============ DIALOG HANDLER ============

    @FXML
    public void openCreateShipmentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/AddShipmentDialog.fxml"));
            Parent root = loader.load();

            AddShipmentController controller = loader.getController();

            // Generate next shipment ID like SH-006
            int nextNum = shipments.size() + 1;
            String nextId = String.format("SH-%03d", nextNum);
            controller.setNextShipmentId(nextId);

            controller.setOnShipmentCreated(shipment -> {
                // 1) Update UI
                shipments.add(shipment);
                shipmentTable.refresh();

                // 2) Persist to Firestore
                saveShipmentToFirestore(shipment);
            });

            Stage dialog = new Stage();
            dialog.initOwner(shipmentTable.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Create New Shipment");
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Add Shipment dialog:\n" + e.getMessage());
        }
    }

    // ============ UTIL ============

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
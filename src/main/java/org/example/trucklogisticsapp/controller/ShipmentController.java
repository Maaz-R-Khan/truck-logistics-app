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
import java.text.NumberFormat;

public class ShipmentController {

    @FXML private TableView<Shipment> shipmentTable;

    @FXML private TableColumn<Shipment, String> idColumn;
    @FXML private TableColumn<Shipment, String> routeColumn;
    @FXML private TableColumn<Shipment, String> customerColumn;
    @FXML private TableColumn<Shipment, String> weightColumn;
    @FXML private TableColumn<Shipment, Integer> valueColumn;
    @FXML private TableColumn<Shipment, String> priorityColumn;
    @FXML private TableColumn<Shipment, String> statusColumn;
    @FXML private TableColumn<Shipment, String> assignmentColumn;
    @FXML private TableColumn<Shipment, String> deliveryColumn;
    @FXML private TableColumn<Shipment, Void> actionsColumn;

    @FXML private Label totalShipmentsLabel;
    @FXML private Label inTransitLabel;
    @FXML private Label pendingLabel;
    @FXML private Label totalValueLabel;

    private final ObservableList<Shipment> shipments = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        configurePriorityColumn();
        configureStatusColumn();
        configureActionsColumn();

        shipmentTable.setItems(shipments);

        loadShipmentsFromFirestore();
        refreshStats();
    }

    // ==================================================
    // SHIPMENT STATS
    // ==================================================
    private void refreshStats() {
        Firestore db = FirestoreContext.getDB();
        ApiFuture<QuerySnapshot> fut = db.collection("shipments").get();

        new Thread(() -> {
            try {
                QuerySnapshot snap = fut.get();

                int total = snap.size();
                int inTransit = 0;
                int pending = 0;
                double totalValue = 0;

                for (DocumentSnapshot doc : snap.getDocuments()) {

                    Object statusObj = doc.get("status");
                    String status = statusObj != null ? statusObj.toString() : null;

                    if (status != null) {
                        String s = status.toLowerCase().trim();
                        if (s.equals("in transit") || s.equals("intransit") || s.equals("transit"))
                            inTransit++;
                        if (s.equals("pending"))
                            pending++;
                    }

                    Object v = doc.get("totalValue");
                    if (v == null) v = doc.get("value");
                    if (v instanceof Number) {
                        totalValue += ((Number) v).doubleValue();
                    }
                }

                final int fTotal = total;
                final int fInTransit = inTransit;
                final int fPending = pending;
                final double fTotalValue = totalValue;

                Platform.runLater(() -> {
                    NumberFormat money = NumberFormat.getCurrencyInstance();

                    totalShipmentsLabel.setText(String.valueOf(fTotal));
                    inTransitLabel.setText(String.valueOf(fInTransit));
                    pendingLabel.setText(String.valueOf(fPending));
                    totalValueLabel.setText(money.format(fTotalValue));
                });

            } catch (Exception e) {
                System.err.println("Failed stats: " + e.getMessage());
            }
        }).start();
    }

    // ==================================================
    // TABLE BINDING
    // ==================================================
    private void setupTableColumns() {

        idColumn.setCellValueFactory(d -> d.getValue().shipmentIdProperty());
        routeColumn.setCellValueFactory(d -> d.getValue().routeProperty());
        customerColumn.setCellValueFactory(d -> d.getValue().customerProperty());
        weightColumn.setCellValueFactory(d -> d.getValue().weightProperty());
        valueColumn.setCellValueFactory(c -> c.getValue().valueProperty().asObject());
        priorityColumn.setCellValueFactory(d -> d.getValue().priorityProperty());
        statusColumn.setCellValueFactory(d -> d.getValue().statusProperty());
        assignmentColumn.setCellValueFactory(d -> d.getValue().assignmentProperty());
        deliveryColumn.setCellValueFactory(d -> d.getValue().deliveryProperty());

        wrapColumnText(routeColumn);
        wrapColumnText(assignmentColumn);
        wrapColumnText(deliveryColumn);
    }

    private void wrapColumnText(TableColumn<Shipment, String> col) {
        col.setCellFactory(c -> new TableCell<>() {
            private final Label lbl = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    lbl.setText(item);
                    lbl.setWrapText(true);
                    setGraphic(lbl);
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
                    case "High" -> label.getStyleClass().add("badge-high");
                    case "Medium" -> label.getStyleClass().add("badge-medium");
                    case "Urgent" -> label.getStyleClass().add("badge-urgent");
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
                    case "Delivered" -> label.getStyleClass().add("status-delivered");
                    case "Assigned" -> label.getStyleClass().add("status-assigned");
                    case "Pending" -> label.getStyleClass().add("status-pending");
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
                    Shipment s = getTableView().getItems().get(getIndex());
                    System.out.println("Edit " + s.getShipmentId());
                });

                viewBtn.setOnAction(e -> {
                    Shipment s = getTableView().getItems().get(getIndex());
                    System.out.println("View " + s.getShipmentId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // ==================================================
    // LOAD SHIPMENTS
    // ==================================================
    private void loadShipmentsFromFirestore() {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();
                ApiFuture<QuerySnapshot> fut = db.collection("shipments").get();
                QuerySnapshot snap = fut.get();

                ObservableList<Shipment> loaded = FXCollections.observableArrayList();

                for (QueryDocumentSnapshot doc : snap.getDocuments()) {
                    Shipment s = doc.toObject(Shipment.class);

                    if (s.getShipmentId() == null || s.getShipmentId().isEmpty()) {
                        s.setShipmentId(doc.getId());
                    }

                    loaded.add(s);
                }

                Platform.runLater(() -> {
                    shipments.clear();
                    shipments.addAll(loaded);
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not load shipments:\n" + e.getMessage()));
            }
        }).start();
    }

    // ==================================================
    // SAVE
    // ==================================================
    private void saveShipmentToFirestore(Shipment shipment) {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                String docId = shipment.getShipmentId();
                if (docId == null || docId.isEmpty()) {
                    docId = db.collection("shipments").document().getId();
                    shipment.setShipmentId(docId);
                }

                ApiFuture<WriteResult> fut =
                        db.collection("shipments").document(docId).set(shipment);

                WriteResult result = fut.get();
                System.out.println("Saved shipment at " + result.getUpdateTime());

            } catch (Exception e) {
                Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not save shipment:\n" + e.getMessage()));
            }
        }).start();
    }

    // ==================================================
    // CREATE DIALOG
    // ==================================================
    @FXML
    private void openCreateShipmentDialog() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/example/trucklogisticsapp/AddShipmentDialog.fxml"));

            Parent root = loader.load();
            AddShipmentController controller = loader.getController();

            String nextId = String.format("SH-%03d", shipments.size() + 1);
            controller.setNextShipmentId(nextId);

            controller.setOnShipmentCreated(shipment -> {
                shipments.add(shipment);
                shipmentTable.refresh();
                saveShipmentToFirestore(shipment);
            });

            Stage dialog = new Stage();
            dialog.initOwner(shipmentTable.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Create Shipment");
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Add Shipment dialog:\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}

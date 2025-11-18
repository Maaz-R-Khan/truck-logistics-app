package org.example.trucklogisticsapp.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.trucklogisticsapp.model.Shipment;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ShipmentController {

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

    private final ObservableList<Shipment> shipments =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {
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

        // Badge-style cells for priority and status
        configurePriorityColumn();
        configureStatusColumn();

        // Actions column (edit / view buttons)
        configureActionsColumn();

        // Sample data to match the screenshot
        shipments.addAll(
                new Shipment("SH-001",
                        "Los Angeles, CA\nPhoenix, AZ",
                        "ABC Electronics",
                        "15,000 lbs",
                        "$ 50,000",
                        "High",
                        "In Transit",
                        "TRK-001\nJohn Smith",
                        "2024-10-07\n8 hours"),
                new Shipment("SH-002",
                        "Denver, CO\nSeattle, WA",
                        "Northwest Manufacturing",
                        "22,000 lbs",
                        "$ 75,000",
                        "Medium",
                        "Delivered",
                        "TRK-002\nSarah Johnson",
                        "2024-10-06\n12 hours"),
                new Shipment("SH-003",
                        "Dallas, TX\nMiami, FL",
                        "Sunshine Imports",
                        "18,500 lbs",
                        "$ 65,000",
                        "High",
                        "Assigned",
                        "TRK-003\nMike Wilson",
                        "2024-10-08\n14 hours"),
                new Shipment("SH-004",
                        "Chicago, IL\nNew York, NY",
                        "Metro Supply Co.",
                        "12,000 lbs",
                        "$ 35,000",
                        "Medium",
                        "Pending",
                        "Unassigned",
                        "2024-10-09\n10 hours"),
                new Shipment("SH-005",
                        "Atlanta, GA\nHouston, TX",
                        "Southern Logistics",
                        "20,000 lbs",
                        "$ 45,000",
                        "Urgent",
                        "Pending",
                        "Unassigned",
                        "2024-10-09\n11 hours")
        );

        shipmentTable.setItems(shipments);
    }

    private void wrapColumnText(TableColumn<Shipment, String> column) {
        column.setCellFactory(col -> {
            TableCell<Shipment, String> cell = new TableCell<>() {
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
            };
            return cell;
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
                    default -> { }
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
                    default -> { }
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

            controller.setOnShipmentCreated(shipments::add);

            Stage dialog = new Stage();
            dialog.initOwner(shipmentTable.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Create New Shipment");
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


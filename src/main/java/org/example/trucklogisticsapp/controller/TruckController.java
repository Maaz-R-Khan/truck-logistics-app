package org.example.trucklogisticsapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.trucklogisticsapp.model.Truck;

public class TruckController {

    @FXML private TableView<Truck> truckTable;
    @FXML private TableColumn<Truck, String> colTruckId;
    @FXML private TableColumn<Truck, String> colPlate;
    @FXML private TableColumn<Truck, String> colModel;
    @FXML private TableColumn<Truck, String> colCapacity;
    @FXML private TableColumn<Truck, String> colFuelMpg;
    @FXML private TableColumn<Truck, String> colStatus;
    @FXML private TableColumn<Truck, String> colDriver;
    @FXML private TableColumn<Truck, Double> colMileage;
    @FXML private TableColumn<Truck, Void> colActions;

    @FXML private Text lblTotalTrucks;
    @FXML private Text lblAvailable;
    @FXML private Text lblAvgCapacity;

    private ObservableList<Truck> truckList;

    @FXML
    public void initialize() {
        truckList = FXCollections.observableArrayList();

        // Set up columns
        colTruckId.setCellValueFactory(cellData ->
                new SimpleStringProperty("TRK-" + cellData.getValue().getId().substring(0, 3).toUpperCase()));

        colPlate.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));

        colModel.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMake() + " " + cellData.getValue().getModel()));

        colCapacity.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%,.0f lbs", cellData.getValue().getCapacityKg() * 2.20462)));

        colFuelMpg.setCellValueFactory(cellData ->
                new SimpleStringProperty("⛽ " + String.format("%.1f", 6.5 + Math.random() * 1.5)));

        colStatus.setCellValueFactory(cellData -> {
            Truck truck = cellData.getValue();
            if (truck.isNeedsMaintenance()) {
                return new SimpleStringProperty("Maintenance");
            } else if (!truck.isAvailable()) {
                return new SimpleStringProperty("In Transit");
            } else {
                return new SimpleStringProperty("Available");
            }
        });

        // Custom cell factory for status with colored badges
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
                        case "In Transit":
                            badge.getStyleClass().add("status-in-transit");
                            break;
                        case "Maintenance":
                            badge.getStyleClass().add("status-maintenance");
                            break;
                        case "Loading":
                            badge.getStyleClass().add("status-loading");
                            break;
                    }

                    setGraphic(badge);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        colDriver.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isAvailable() ? "Unassigned" : "John Smith"));

        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        colMileage.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double mileage, boolean empty) {
                super.updateItem(mileage, empty);
                if (empty || mileage == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", mileage));
                }
            }
        });

        // Actions column with edit icon
        colActions.setCellFactory(column -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");

            {
                editBtn.getStyleClass().add("edit-button");
                editBtn.setOnAction(event -> {
                    Truck truck = getTableView().getItems().get(getIndex());
                    handleEdit(truck);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editBtn);
                setAlignment(Pos.CENTER);
            }
        });

        truckTable.setItems(truckList);
        loadSampleData();
        updateStats();
    }

    @FXML
    private void handleAdd() {
        // Open dialog to add new truck
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Truck");
        alert.setHeaderText("Add New Truck");
        alert.setContentText("This would open a dialog to add a new truck.");
        alert.showAndWait();
    }

    private void handleEdit(Truck truck) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Truck");
        alert.setHeaderText("Edit Truck: " + truck.getDisplayName());
        alert.setContentText("This would open a dialog to edit the truck.");
        alert.showAndWait();
    }

    private void loadSampleData() {
        Truck t1 = new Truck("ABC-1234", "Freightliner", "Cascadia", 2020, 11793);
        t1.setPlateNumber("ABC-1234");
        t1.setMileage(125000);
        t1.setAvailable(false);

        Truck t2 = new Truck("XYZ-5678", "Volvo", "VNL", 2021, 10886);
        t2.setPlateNumber("XYZ-5678");
        t2.setMileage(98000);
        t2.setAvailable(true);

        Truck t3 = new Truck("DEF-9012", "Peterbilt", "579", 2019, 12701);
        t3.setPlateNumber("DEF-9012");
        t3.setMileage(156000);
        t3.setNeedsMaintenance(true);

        Truck t4 = new Truck("GHI-3456", "Kenworth", "T680", 2022, 11340);
        t4.setPlateNumber("GHI-3456");
        t4.setMileage(87000);
        t4.setAvailable(false);

        truckList.addAll(t1, t2, t3, t4);
    }

    private void updateStats() {
        lblTotalTrucks.setText(String.valueOf(truckList.size()));

        long available = truckList.stream()
                .filter(t -> t.isAvailable() && !t.isNeedsMaintenance())
                .count();
        lblAvailable.setText(String.valueOf(available));

        double avgCapacity = truckList.stream()
                .mapToDouble(Truck::getCapacityKg)
                .average()
                .orElse(0.0);
        lblAvgCapacity.setText(String.format("%,.0f", avgCapacity * 2.20462));
    }
}
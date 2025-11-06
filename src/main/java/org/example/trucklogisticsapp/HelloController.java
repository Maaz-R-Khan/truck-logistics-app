package org.example.trucklogisticsapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;

public class HelloController {

    // Metric cards
    @FXML
    private Label lblActiveTrucks;
    @FXML private Label lblActiveTrucksDelta;

    @FXML private Label lblAvailableDrivers;
    @FXML private Label lblAvailableDriversDelta;

    @FXML private Label lblPendingShipments;
    @FXML private Label lblPendingShipmentsDelta;

    @FXML private Label lblRoutesOptimized;
    @FXML private Label lblRoutesOptimizedDelta;

    // Utilization
    @FXML private ProgressBar pbAverageLoad;
    @FXML private ProgressBar pbFuelEfficiency;
    @FXML private ProgressBar pbOnTimeDelivery;

    @FXML private Label lblAverageLoadPct;
    @FXML private Label lblFuelEfficiencyPct;
    @FXML private Label lblOnTimeDeliveryPct;

    // Recent shipments
    @FXML private ListView<String> lvRecentShipments;
    @FXML private TextField tfShipmentSearch;

    @FXML
    private void initialize() {
        // TODO wire real data service here.
        seedDemoData();
    }

    private void seedDemoData() {
        // Metrics
        setMetric(lblActiveTrucks, lblActiveTrucksDelta, 24, "+2 from last week");
        setMetric(lblAvailableDrivers, lblAvailableDriversDelta, 18, "+1 from last week");
        setMetric(lblPendingShipments, lblPendingShipmentsDelta, 42, "-5 from last week");
        setMetric(lblRoutesOptimized, lblRoutesOptimizedDelta, 156, "+12 from last week");

        // Utilization (0.0–1.0)
        setBar(pbAverageLoad, lblAverageLoadPct, 0.78);
        setBar(pbFuelEfficiency, lblFuelEfficiencyPct, 0.92);
        setBar(pbOnTimeDelivery, lblOnTimeDeliveryPct, 0.85);

        // Recent shipments
        lvRecentShipments.getItems().setAll(
                "SH001 — Los Angeles, CA — In Transit — ETA 2h (John Smith)",
                "SH002 — Phoenix, AZ — Delivered — Completed (Sarah Johnson)",
                "SH003 — Denver, CO — Loading — ETA 4h (Mike Wilson)",
                "SH004 — Seattle, WA — Pending — ETA 6h (Unassigned)"
        );
    }

    private void setMetric(Label valueLabel, Label deltaLabel, int value, String deltaText) {
        valueLabel.setText(Integer.toString(value));
        deltaLabel.setText(deltaText);
    }

    private void setBar(ProgressBar bar, Label pctLabel, double progress) {
        bar.setProgress(progress);
        pctLabel.setText((int)Math.round(progress * 100) + "%");
    }

    // ----- Actions -----

    @FXML
    private void handleRefresh() {
        // In the real app, re-fetch from services and update UI.
        // For now, just simulate a quick refresh.
        Platform.runLater(this::seedDemoData);
    }

    @FXML
    private void handleSearchShipments() {
        String q = tfShipmentSearch.getText() == null ? "" : tfShipmentSearch.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            seedDemoData();
            return;
        }
        lvRecentShipments.getItems().removeIf(item -> !item.toLowerCase().contains(q));
    }

    @FXML
    private void handleOpenShipment() {
        String selected = lvRecentShipments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Please select a shipment to open.");
            return;
        }
        // TODO: route to shipment detail screen
        showInfo("Open shipment: " + selected);
    }

    @FXML
    private void handleNewShipment() {
        // TODO: navigate to "create shipment" workflow
        showInfo("New shipment flow starting…");
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.showAndWait();
    }
}


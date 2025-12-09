package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DashboardController {

    @FXML private Text lblTotalTrucks;
    @FXML private Text lblAvailableDrivers;
    @FXML private Text lblPendingShipments;

    @FXML
    public void initialize() {
        System.out.println("✅ DashboardController initialized");
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Load sample data
        lblTotalTrucks.setText("9");
        lblAvailableDrivers.setText("18");
        lblPendingShipments.setText("5");

        System.out.println("📊 Dashboard data loaded");
    }
}
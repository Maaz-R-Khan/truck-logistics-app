package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DashboardController {

    @FXML private Text lblActiveTrucks;
    @FXML private Text lblAvailableDrivers;
    @FXML private Text lblPendingShipments;
    @FXML private Text lblRoutesOptimized;

    @FXML
    public void initialize() {
        System.out.println("✅ DashboardController initialized");
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Load sample data
        lblActiveTrucks.setText("24");
        lblAvailableDrivers.setText("18");
        lblPendingShipments.setText("42");
        lblRoutesOptimized.setText("156");

        System.out.println("📊 Dashboard data loaded");
    }
}
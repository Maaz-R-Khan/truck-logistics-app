package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Main Layout Controller - Handles navigation between different screens
 */
public class MainLayoutController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Load dashboard by default
        // showDashboard();

        // For now, load Truck Management as default
        showTrucks();
    }

    @FXML
    private void showDashboard() {
        loadView("Dashboard.fxml"); // Create this later
    }

    @FXML
    private void showTrucks() {
        loadView("TruckManagement.fxml");
    }

    @FXML
    private void showDrivers() {
        loadView("DriverManagement.fxml");
    }

    @FXML
    private void showShipments() {
        loadView("ShipmentManagement.fxml"); // Create this later
    }

    @FXML
    private void showRoutePlanner() {
        loadView("RoutePlanner.fxml"); // Create this later
    }

    @FXML
    private void showSettings() {
        loadView("Settings.fxml"); // Create this later
    }

    /**
     * Load a view into the content area
     */
    private void loadView(String fxmlFile) {
        try {
            // Load from package path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/trucklogisticsapp/" + fxmlFile));
            Parent view = loader.load();

            // Clear current content and add new view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

            System.out.println("✅ Loaded: " + fxmlFile);
        } catch (IOException e) {
            System.err.println("❌ Failed to load: " + fxmlFile);
            e.printStackTrace();

            // Show error message in content area
            javafx.scene.control.Label errorLabel = new javafx.scene.control.Label(
                    "View not found: " + fxmlFile + "\n\nCreate this view to see content here."
            );
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6B7280;");
            contentArea.getChildren().clear();
            contentArea.getChildren().add(errorLabel);
        }
    }
}
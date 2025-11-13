package org.example.trucklogisticsapp.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainLayoutController {

    @FXML private StackPane contentArea;

    @FXML private Button btnDashboard;
    @FXML private Button btnTrucks;
    @FXML private Button btnDrivers;
    @FXML private Button btnShipments;
    @FXML private Button btnRoutePlanner;
    @FXML private Button btnMaintenance;
    @FXML private Button btnSettings;

    @FXML
    public void initialize() {
        System.out.println("✅ MainLayoutController initialized");
        loadDashboard(); // Load default view
    }

    // ------------------------- VIEW LOADERS ---------------------------- //

    @FXML
    private void loadDashboard() {
        System.out.println("📊 Loading Dashboard...");
        setActiveButton(btnDashboard);
        loadView("/org/example/trucklogisticsapp/Dashboard.fxml");
    }

    @FXML
    private void loadTrucks() {
        System.out.println("🚛 Loading Trucks...");
        setActiveButton(btnTrucks);
        loadView("/org/example/trucklogisticsapp/TruckManagement.fxml");
    }

    @FXML
    private void loadDrivers() {
        System.out.println("👤 Loading Drivers...");
        setActiveButton(btnDrivers);
        showPlaceholder("👤 Driver Management - Coming Soon!");
    }

    @FXML
    private void loadShipments() {
        System.out.println("📦 Loading Shipments...");
        setActiveButton(btnShipments);
        showPlaceholder("📦 Shipment Management - Coming Soon!");
    }

    @FXML
    private void loadRoutePlanner() {
        System.out.println("🗺️ Loading Route Planner...");
        setActiveButton(btnRoutePlanner);
        showPlaceholder("🗺️ Route Planner - Coming Soon!");
    }

    @FXML
    private void loadMaintenance() {
        System.out.println("🛠️ Loading Maintenance...");
        setActiveButton(btnMaintenance);
        System.out.println(getClass().getResource("/org/example/trucklogisticsapp/MaintenanceManagement.fxml"));
        loadView("/org/example/trucklogisticsapp/MaintenanceManagement.fxml");
    }


    @FXML
    private void loadSettings() {
        System.out.println("⚙️ Loading Settings...");
        setActiveButton(btnSettings);
        showPlaceholder("⚙️ Settings - Coming Soon!");
    }

    // ------------------------- VIEW HANDLING ---------------------------- //

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            // Fade animation
            view.setOpacity(0);
            contentArea.getChildren().setAll(view);

            FadeTransition fade = new FadeTransition(Duration.millis(300), view);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();

            System.out.println("✅ View loaded successfully: " + fxmlPath);

        } catch (Exception e) {
            System.err.println("❌ ERROR loading view: " + fxmlPath);
            e.printStackTrace();
            showError("Failed to load: " + fxmlPath);
        }
    }

    // ------------------------- ACTIVE BUTTON STATE ---------------------------- //

    private void setActiveButton(Button activeButton) {

        // Remove highlight from all buttons
        Button[] buttons = {
                btnDashboard, btnTrucks, btnDrivers,
                btnShipments, btnRoutePlanner, btnMaintenance, btnSettings
        };

        for (Button btn : buttons) {
            btn.getStyleClass().remove("nav-button-active");
        }

        // Add highlight to clicked button
        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }

    // ------------------------- PLACEHOLDER HELPERS ---------------------------- //

    private void showPlaceholder(String message) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(message);
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #9CA3AF;");

        StackPane pane = new StackPane(label);
        contentArea.getChildren().setAll(pane);
    }

    private void showError(String message) {
        javafx.scene.control.Label label = new javafx.scene.control.Label("❌ " + message);
        label.setStyle("-fx-font-size: 18px; -fx-text-fill: #EF4444;");

        StackPane pane = new StackPane(label);
        contentArea.getChildren().setAll(pane);
    }

}

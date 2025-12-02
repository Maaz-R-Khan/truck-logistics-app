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
    @FXML private Button btnSettings;
    @FXML private Button btnMaintenance;   // ✅ NEW

    @FXML
    public void initialize() {
        System.out.println("✅ MainLayoutController initialized");
        // Load dashboard by default
        loadDashboard();
    }

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
        loadView("/org/example/trucklogisticsapp/DriverManagement.fxml");
    }

    @FXML
    private void loadShipments() {
        System.out.println("📦 Loading Shipments...");
        setActiveButton(btnShipments);
        loadView("/org/example/trucklogisticsapp/ShipmentManagement.fxml");
    }

    @FXML
    private void loadRoutePlanner() {
        System.out.println("🗺️ Loading Route Planner...");
        setActiveButton(btnRoutePlanner);
        showPlaceholder("🗺️ Route Planner - Coming Soon!");
    }

    @FXML
    private void loadSettings() {
        System.out.println("⚙️ Loading Settings...");
        setActiveButton(btnSettings);
        showPlaceholder("⚙️ Settings - Coming Soon!");
    }

    @FXML
    private void loadMaintenance() {        // ✅ FIXED
        System.out.println("🛠️ Loading Maintenance...");
        setActiveButton(btnMaintenance);
        loadView("/org/example/trucklogisticsapp/MaintenanceManagement.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            var url = getClass().getResource(fxmlPath);
            if (url == null) {
                throw new IllegalStateException("FXML not found on classpath: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();

            // Fade in animation
            view.setOpacity(0);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

            FadeTransition fade = new FadeTransition(Duration.millis(300), view);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();

            System.out.println("✅ View loaded successfully: " + fxmlPath);

        } catch (Exception e) {
            System.err.println("❌ ERROR loading view: " + fxmlPath);
            e.printStackTrace();
            showError("Failed to load view: " + e.getMessage());
        }
    }

    private void setActiveButton(Button activeButton) {
        // Remove active class from all buttons
        btnDashboard.getStyleClass().remove("nav-button-active");
        btnTrucks.getStyleClass().remove("nav-button-active");
        btnDrivers.getStyleClass().remove("nav-button-active");
        btnShipments.getStyleClass().remove("nav-button-active");
        btnRoutePlanner.getStyleClass().remove("nav-button-active");
        btnSettings.getStyleClass().remove("nav-button-active");
        btnMaintenance.getStyleClass().remove("nav-button-active"); // ✅ added

        // Add active class to clicked button
        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }

    private void showPlaceholder(String message) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(message);
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #9CA3AF;");
        javafx.scene.layout.StackPane pane = new javafx.scene.layout.StackPane(label);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(pane);
    }

    private void showError(String message) {
        javafx.scene.control.Label label = new javafx.scene.control.Label("❌ " + message);
        label.setStyle("-fx-font-size: 18px; -fx-text-fill: #EF4444;");
        javafx.scene.layout.StackPane pane = new javafx.scene.layout.StackPane(label);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(pane);
    }
}

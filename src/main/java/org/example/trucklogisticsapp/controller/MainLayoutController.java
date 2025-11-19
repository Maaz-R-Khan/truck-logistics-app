package org.example.trucklogisticsapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Main Layout Controller - Handles navigation between different views
 */
public class MainLayoutController {

    @FXML private StackPane contentArea;

    @FXML private Button btnDashboard;
    @FXML private Button btnTrucks;
    @FXML private Button btnDrivers;
    @FXML private Button btnShipments;
    @FXML private Button btnRoutePlanner;
    @FXML private Button btnSettings;

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
        loadView("Dashboard.fxml");
    }

    @FXML
    private void loadTrucks() {
        System.out.println("🚛 Loading Trucks...");
        setActiveButton(btnTrucks);
        loadView("TruckManagement.fxml");
    }

    @FXML
    private void loadDrivers() {
        System.out.println("👤 Loading Drivers...");
        setActiveButton(btnDrivers);
        loadView("DriverManagement.fxml");
    }

    @FXML
    private void loadShipments() {
        System.out.println("📦 Loading Shipments...");
        setActiveButton(btnShipments);
        showComingSoon("Shipments");
    }

    @FXML
    private void loadRoutePlanner() {
        System.out.println("🗺️ Loading Route Planner...");
        setActiveButton(btnRoutePlanner);
        showComingSoon("Route Planner");
    }

    @FXML
    private void loadSettings() {
        System.out.println("⚙️ Loading Settings...");
        setActiveButton(btnSettings);
        showComingSoon("Settings");
    }

    /**
     * Load a view into the content area
     */
    private void loadView(String fxmlFile) {
        try {
            System.out.println("🔄 Attempting to load: " + fxmlFile);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/" + fxmlFile)
            );

            Parent view = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

            System.out.println("✅ Successfully loaded: " + fxmlFile);

        } catch (IOException e) {
            System.err.println("❌ Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();

            showError("Failed to load " + fxmlFile,
                    "Could not load the view. Please check that the FXML file exists:\n" +
                            "/src/main/resources/org/example/trucklogisticsapp/" + fxmlFile + "\n\n" +
                            "Error: " + e.getMessage());
        }
    }

    /**
     * Set the active navigation button
     */
    private void setActiveButton(Button activeButton) {
        // Remove active class from all buttons
        btnDashboard.getStyleClass().remove("nav-button-active");
        btnTrucks.getStyleClass().remove("nav-button-active");
        btnDrivers.getStyleClass().remove("nav-button-active");
        btnShipments.getStyleClass().remove("nav-button-active");
        btnRoutePlanner.getStyleClass().remove("nav-button-active");
        btnSettings.getStyleClass().remove("nav-button-active");

        // Add active class to the clicked button
        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }

        System.out.println("✅ Set active button: " + activeButton.getText());
    }

    /**
     * Show a "Coming Soon" message for unimplemented features
     */
    private void showComingSoon(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText(featureName + " - Coming Soon!");
        alert.setContentText(
                "The " + featureName + " feature is currently under development.\n\n" +
                        "Available features:\n" +
                        "  • Dashboard ✓\n" +
                        "  • Trucks ✓\n" +
                        "  • Drivers ✓\n\n" +
                        "Stay tuned for updates!"
        );
        alert.showAndWait();
    }

    /**
     * Show an error dialog
     */
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
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
    @FXML private Button btnMaintenance;

    @FXML
    public void initialize() {
        System.out.println("✅ MainLayoutController initialized");
        loadDashboard(); // Default screen
    }

    @FXML
    private void loadDashboard() {
        setActiveButton(btnDashboard);
        loadView("/org/example/trucklogisticsapp/Dashboard.fxml");
    }

    @FXML
    private void loadTrucks() {
        setActiveButton(btnTrucks);
        loadView("/org/example/trucklogisticsapp/TruckManagement.fxml");
    }

    @FXML
    private void loadDrivers() {
        setActiveButton(btnDrivers);
        loadView("/org/example/trucklogisticsapp/DriverManagement.fxml");
    }

    @FXML
    private void loadShipments() {
        setActiveButton(btnShipments);
        loadView("/org/example/trucklogisticsapp/ShipmentManagement.fxml");
    }

    @FXML
    private void loadMaintenance() {
        setActiveButton(btnMaintenance);
        loadView("/org/example/trucklogisticsapp/MaintenanceManagement.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            var url = getClass().getResource(fxmlPath);
            if (url == null)
                throw new IllegalStateException("FXML not found: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();

            view.setOpacity(0);
            contentArea.getChildren().setAll(view);

            FadeTransition fade = new FadeTransition(Duration.millis(300), view);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load view: " + e.getMessage());
        }
    }

    private void setActiveButton(Button activeButton) {
        btnDashboard.getStyleClass().remove("nav-button-active");
        btnTrucks.getStyleClass().remove("nav-button-active");
        btnDrivers.getStyleClass().remove("nav-button-active");
        btnShipments.getStyleClass().remove("nav-button-active");
        btnMaintenance.getStyleClass().remove("nav-button-active");

        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }

    private void showError(String message) {
        javafx.scene.control.Label label = new javafx.scene.control.Label("❌ " + message);
        label.setStyle("-fx-font-size: 18px; -fx-text-fill: #EF4444;");
        javafx.scene.layout.StackPane pane = new javafx.scene.layout.StackPane(label);

        contentArea.getChildren().setAll(pane);
    }
}

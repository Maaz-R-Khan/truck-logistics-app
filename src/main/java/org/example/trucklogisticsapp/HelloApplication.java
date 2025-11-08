package org.example.trucklogisticsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load Main Layout with Sidebar Navigation
            testMainLayout(primaryStage);

            // OR test individual screens:
            // testTruckManagement(primaryStage);
            // testDriverManagement(primaryStage);

        } catch (Exception e) {
            System.err.println("Error loading application:");
            e.printStackTrace();

            // Show detailed error information
            showErrorDetails(e);
        }
    }


    private void testMainLayout(Stage primaryStage) throws Exception {
        System.out.println("Loading Main Layout with Navigation...");

        // Load from package path
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/trucklogisticsapp/MainLayout.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1600, 1000);

        primaryStage.setTitle("LogiTruck - Truck Logistics System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        System.out.println("✅ Main Layout loaded successfully!");
    }

    /**
     * Test Truck Management Screen
     */
    private void testTruckManagement(Stage primaryStage) throws Exception {
        System.out.println("Loading Truck Management...");
        System.out.println("Looking for: /org/example/trucklogisticsapp/TruckManagement.fxml");

        // Try to find the FXML file (try both root and package location)
        java.net.URL fxmlUrl = getClass().getResource("/TruckManagement.fxml");

        // If not found in root, try package structure
        if (fxmlUrl == null) {
            fxmlUrl = getClass().getResource("/org/example/trucklogisticsapp/TruckManagement.fxml");
        }

        if (fxmlUrl == null) {
            System.err.println("❌ FXML file not found!");
            System.err.println("Checking alternative locations...");

            // Try without leading slash
            fxmlUrl = getClass().getResource("TruckManagement.fxml");
            if (fxmlUrl == null) {
                System.err.println("❌ Still not found!");
                System.err.println("\nPlease ensure TruckManagement.fxml is in:");
                System.err.println("  src/main/resources/TruckManagement.fxml");
                throw new Exception("FXML file not found in resources folder");
            }
        }

        System.out.println("✅ Found FXML at: " + fxmlUrl);

        // Load FXML
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();



        // Create scene
        Scene scene = new Scene(root, 1400, 900);

        // Set up stage
        primaryStage.setTitle("LogiTruck - Truck Management");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Start maximized
        primaryStage.show();

        System.out.println("✅ Truck Management loaded successfully!");
    }

    /**
     * Test Driver Management Screen
     */
    private void testDriverManagement(Stage primaryStage) throws Exception {
        System.out.println("Loading Driver Management...");

        // Load from package path
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/trucklogisticsapp/DriverManagement.fxml"));
        Parent root = loader.load();

        // Get controller (optional)
        // DriverController controller = loader.getController();

        // Create scene
        Scene scene = new Scene(root, 1400, 900);

        // Set up stage
        primaryStage.setTitle("LogiTruck - Driver Management");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        System.out.println("✅ Driver Management loaded successfully!");
    }

    /**
     * Show detailed error information for debugging
     */
    private void showErrorDetails(Exception e) {
        System.err.println("\n=== ERROR DETAILS ===");
        System.err.println("Error Type: " + e.getClass().getName());
        System.err.println("Message: " + e.getMessage());
        System.err.println("\nStack Trace:");
        e.printStackTrace();
        System.err.println("\n=== COMMON FIXES ===");
        System.err.println("1. Check that FXML files are in src/main/resources/");
        System.err.println("2. Check that CSS file is in src/main/resources/");
        System.err.println("3. Verify fx:controller package path in FXML");
        System.err.println("4. Ensure all @FXML fields match fx:id in FXML");
        System.err.println("5. Check for typos in file names and paths");
    }

    public static void main(String[] args) {
        System.out.println("Starting LogiTruck Application...");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("JavaFX Version: " + System.getProperty("javafx.version"));
        System.out.println();

        launch(args);
    }
}
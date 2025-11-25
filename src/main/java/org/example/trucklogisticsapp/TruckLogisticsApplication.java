package org.example.trucklogisticsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.config.FirebaseConfig;

import java.io.IOException;

public class TruckLogisticsApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Initialize Firebase BEFORE loading UI
        FirebaseConfig.initializeFirebase();

        FXMLLoader fxmlLoader = new FXMLLoader(
                TruckLogisticsApplication.class.getResource("/org/example/trucklogisticsapp/MainLayout.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Truck Logistics Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
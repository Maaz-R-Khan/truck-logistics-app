module org.example.trucklogisticsapp {
    // JavaFX requirements
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Optional UI libraries
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires google.cloud.firestore;

    // For TMS/EDI integration (add when ready)
    // requires okhttp3;
    // requires com.google.gson;

    // CRITICAL: Open your packages to JavaFX FXML
    // ← THIS WAS MISSING!
    opens org.example.trucklogisticsapp.model to javafx.base;       // ← For TableView binding

    // Export packages
    exports org.example.trucklogisticsapp;
    exports org.example.trucklogisticsapp.controller;
    exports org.example.trucklogisticsapp.model;
    opens org.example.trucklogisticsapp.controller to javafx.base, javafx.fxml;
    opens org.example.trucklogisticsapp to javafx.base, javafx.fxml;

}
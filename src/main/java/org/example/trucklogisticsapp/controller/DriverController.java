package org.example.trucklogisticsapp.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.Driver;

import java.time.LocalDate;
import java.util.Optional;

public class DriverController {

    @FXML private Text lblTotalDrivers;
    @FXML private Text lblAvailableDrivers;
    @FXML private Text lblAvgRating;

    @FXML private TableView<Driver> driverTable;
    @FXML private TableColumn<Driver, String> colDriverId;
    @FXML private TableColumn<Driver, String> colName;
    @FXML private TableColumn<Driver, String> colLicense;
    @FXML private TableColumn<Driver, String> colPhone;
    @FXML private TableColumn<Driver, String> colStatus;
    @FXML private TableColumn<Driver, String> colCompliance;
    @FXML private TableColumn<Driver, String> colEndorsements;
    @FXML private TableColumn<Driver, Void> colActions;

    private final ObservableList<Driver> driverList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("✅ DriverController initialized");
        setupTable();
        loadDriversFromFirestore();
    }

    // -------------------------------------------------------
    // TABLE SETUP
    // -------------------------------------------------------
    private void setupTable() {

        colDriverId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName())
        );

        colLicense.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getLicenseNumber())
        );

        colPhone.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone())
        );

        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().isAvailable() ? "Available" : "Assigned"
                )
        );

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }
                Label badge = new Label(status);
                badge.getStyleClass().add(status.equals("Available") ?
                        "status-badge-green" : "status-badge-blue");
                setGraphic(badge);
            }
        });

        colCompliance.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getComplianceStatus())
        );

        colCompliance.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String compliance, boolean empty) {
                super.updateItem(compliance, empty);
                if (empty || compliance == null) {
                    setGraphic(null);
                    return;
                }
                Label badge = new Label(compliance);
                if (compliance.equals("Compliant")) {
                    badge.getStyleClass().add("status-badge-green");
                } else if (compliance.contains("Expiring")) {
                    badge.getStyleClass().add("status-badge-yellow");
                } else {
                    badge.getStyleClass().add("status-badge-red");
                }
                setGraphic(badge);
            }
        });

        colEndorsements.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEndorsements())
        );

        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = iconButton("✏️", "#007bff", "Edit driver");
            private final Button btnView = iconButton("👁️", "#17a2b8", "View details");
            private final Button btnAssign = iconButton("🚛", "#28a745", "Assign");

            {
                btnEdit.setOnAction(e -> handleEditDriver(getItemAtIndex()));
                btnView.setOnAction(e -> handleViewDriver(getItemAtIndex()));
                btnAssign.setOnAction(e -> handleAssignDriver(getItemAtIndex()));
            }

            private Driver getItemAtIndex() {
                return getTableView().getItems().get(getIndex());
            }

            private Button iconButton(String icon, String color, String tooltip) {
                Button btn = new Button(icon);
                btn.setStyle("-fx-background-color:" + color +
                        "; -fx-text-fill:white; -fx-font-size:14px; -fx-min-width:32px;" +
                        " -fx-min-height:32px; -fx-background-radius:5; -fx-padding:4;");
                btn.setTooltip(new Tooltip(tooltip));
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new HBox(8, btnEdit, btnView, btnAssign));
            }
        });

        driverTable.setItems(driverList);
    }

    // -------------------------------------------------------
    // LOAD DATA FROM FIRESTORE
    // -------------------------------------------------------
    private void loadDriversFromFirestore() {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();
                ApiFuture<QuerySnapshot> future = db.collection("drivers").get();
                QuerySnapshot snapshot = future.get();

                ObservableList<Driver> loadedDrivers = FXCollections.observableArrayList();

                for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                    Driver driver = doc.toObject(Driver.class);

                    if (driver.getId() == null || driver.getId().isEmpty()) {
                        driver.setId(doc.getId());
                    }

                    loadedDrivers.add(driver);
                }

                Platform.runLater(() -> {
                    driverList.clear();
                    driverList.addAll(loadedDrivers);
                    updateStats();
                    System.out.println("📦 Loaded " + loadedDrivers.size() + " drivers from Firestore");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Firestore Error",
                                "Could not load drivers:\n" + e.getMessage())
                );
            }
        }).start();
    }

    // -------------------------------------------------------
    // SAVE DRIVER TO FIRESTORE
    // -------------------------------------------------------
    private void saveDriverToFirestore(Driver driver) {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                String docId = (driver.getId() != null && !driver.getId().isEmpty())
                        ? driver.getId()
                        : db.collection("drivers").document().getId();

                if (driver.getId() == null || driver.getId().isEmpty()) {
                    driver.setId(docId);
                }

                ApiFuture<WriteResult> future =
                        db.collection("drivers").document(docId).set(driver);

                WriteResult result = future.get();
                System.out.println("✅ Driver saved to Firestore at: " + result.getUpdateTime());

            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Save Error",
                                "Could not save driver:\n" + ex.getMessage())
                );
            }
        }).start();
    }

    // -------------------------------------------------------
    // UI: ADD DRIVER
    // -------------------------------------------------------
    @FXML
    private void handleAddDriver() {
        System.out.println("➕ Add driver clicked");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/AddDriverDialog.fxml")
            );
            Parent root = loader.load();

            AddDriverDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Driver");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(driverTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

// IMPORTANT FIX
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            Driver newDriver = controller.getResult();
            if (newDriver != null) {
                driverList.add(newDriver);
                driverTable.refresh();
                updateStats();
                saveDriverToFirestore(newDriver);
                System.out.println("✅ Driver added: " + newDriver.getFullName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not open Add Driver dialog:\n" + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // EDIT, VIEW, ASSIGN
    // -------------------------------------------------------
    private void handleEditDriver(Driver driver) {
        System.out.println("✏️ Edit driver: " + driver.getId());
        // TODO: Add edit dialog + save changes to Firestore
    }

    private void handleViewDriver(Driver driver) {
        System.out.println("👁️ View driver: " + driver.getId());
        String details = "Driver: " + driver.getFullName() + "\nPhone: " + driver.getPhone();

        showAlert(Alert.AlertType.INFORMATION, "Driver Details", details);
    }

    private void handleAssignDriver(Driver driver) {
        if (!driver.isAvailable()) {
            showAlert(Alert.AlertType.WARNING, "Driver Not Available",
                    driver.getFullName() + " is already assigned.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Assign", "Assignment feature coming soon!");
    }

    // -------------------------------------------------------
    // UTILS
    // -------------------------------------------------------
    private void updateStats() {
        lblTotalDrivers.setText(String.valueOf(driverList.size()));
        lblAvailableDrivers.setText(String.valueOf(driverList.stream().filter(Driver::isAvailable).count()));

        double avgRating = driverList.stream().mapToDouble(Driver::getRating).average().orElse(0);
        lblAvgRating.setText(String.format("%.1f", avgRating));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
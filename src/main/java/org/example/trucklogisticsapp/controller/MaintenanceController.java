package org.example.trucklogisticsapp.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.trucklogisticsapp.model.MaintenanceRecord;

import java.io.IOException;
import java.time.LocalDate;

public class MaintenanceController {

    @FXML private TableView<MaintenanceRecord> maintenanceTable;

    @FXML private TableColumn<MaintenanceRecord, String> colRecordId;
    @FXML private TableColumn<MaintenanceRecord, String> colTruckId;
    @FXML private TableColumn<MaintenanceRecord, String> colType;
    @FXML private TableColumn<MaintenanceRecord, LocalDate> colScheduled;
    @FXML private TableColumn<MaintenanceRecord, Double> colCost;
    @FXML private TableColumn<MaintenanceRecord, String> colStatus;

    private final ObservableList<MaintenanceRecord> records = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colRecordId.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getRecordId()));
        colTruckId.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getTruckId()));
        colType.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getType()));

        colScheduled.setCellValueFactory(e ->
                new SimpleObjectProperty<>(e.getValue().toLocalDate())
        );

        colCost.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getCost()));
        colStatus.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getStatus()));

        maintenanceTable.setItems(records);
        loadMaintenanceFromFirestore();
    }

    private void loadMaintenanceFromFirestore() {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();
                ApiFuture<QuerySnapshot> future = db.collection("maintenance").get();
                QuerySnapshot snap = future.get();

                ObservableList<MaintenanceRecord> list = FXCollections.observableArrayList();

                for (QueryDocumentSnapshot doc : snap.getDocuments()) {
                    MaintenanceRecord rec = doc.toObject(MaintenanceRecord.class);

                    if (rec.getRecordId() == null || rec.getRecordId().isEmpty()) {
                        rec.setRecordId(doc.getId());
                    }
                    list.add(rec);
                }

                Platform.runLater(() -> {
                    records.clear();
                    records.addAll(list);
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        showError("Could not load maintenance records:\n" + e.getMessage()));
            }
        }).start();
    }

    private void saveToFirestore(MaintenanceRecord rec) {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();

                String id = (rec.getRecordId() != null && !rec.getRecordId().isEmpty())
                        ? rec.getRecordId()
                        : db.collection("maintenance").document().getId();

                rec.setRecordId(id);
                db.collection("maintenance").document(id).set(rec).get();

            } catch (Exception ex) {
                Platform.runLater(() ->
                        showError("Could not save record:\n" + ex.getMessage()));
            }
        }).start();
    }

    private void deleteFromFirestore(String id) {
        new Thread(() -> {
            try {
                Firestore db = FirestoreContext.getDB();
                db.collection("maintenance").document(id).delete().get();
            } catch (Exception ex) {
                Platform.runLater(() ->
                        showError("Could not delete record:\n" + ex.getMessage()));
            }
        }).start();
    }

    @FXML
    private void onAddRecord() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/AddMaintenanceDialog.fxml"));

            Parent root = loader.load();
            AddMaintenanceController dialog = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add Maintenance Record");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            MaintenanceRecord rec = dialog.getNewRecord();

            if (rec != null) {
                records.add(rec);
                saveToFirestore(rec);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditRecord() {
        MaintenanceRecord selected = maintenanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a record to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/trucklogisticsapp/EditMaintenanceDialog.fxml"));

            Parent root = loader.load();
            EditMaintenanceController dialog = loader.getController();
            dialog.setRecord(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Maintenance Record");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh table AND save changes
            maintenanceTable.refresh();
            saveToFirestore(selected);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteRecord() {
        MaintenanceRecord selected = maintenanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        records.remove(selected);
        deleteFromFirestore(selected.getRecordId());
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Firestore Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}

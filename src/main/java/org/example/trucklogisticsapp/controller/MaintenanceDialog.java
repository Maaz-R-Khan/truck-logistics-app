package org.example.trucklogisticsapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.trucklogisticsapp.model.MaintenanceResult;
import org.example.trucklogisticsapp.model.Truck;

import java.time.LocalDate;

/**
 * Dialog for setting up maintenance reminders for trucks
 */
public class MaintenanceDialog extends Dialog<MaintenanceResult> {

    private final DatePicker lastMaintenanceDatePicker;
    private final ComboBox<Integer> frequencyComboBox;
    private final TextField customFrequencyField;
    private final CheckBox enableRemindersCheckBox;
    private final Spinner<Integer> reminderDaysBeforeSpinner;
    private final Label nextDueLabel;
    private final Truck truck;

    public MaintenanceDialog(Truck truck) {
        this.truck = truck;

        setTitle("⚙️ Maintenance Reminder Setup");
        setHeaderText("Set up maintenance reminders for " + truck.getDisplayName());

        // Initialize controls
        lastMaintenanceDatePicker = new DatePicker();
        lastMaintenanceDatePicker.setValue(truck.getLastMaintenanceDate() != null ?
                truck.getLastMaintenanceDate() : LocalDate.now());

        frequencyComboBox = new ComboBox<>();
        frequencyComboBox.getItems().addAll(1, 3, 6, 12, 24);
        frequencyComboBox.setPromptText("Select interval");
        frequencyComboBox.setValue(truck.getMaintenanceIntervalMonths() > 0 ?
                truck.getMaintenanceIntervalMonths() : 6);

        customFrequencyField = new TextField();
        customFrequencyField.setPromptText("Or enter custom months");
        customFrequencyField.setPrefWidth(150);

        enableRemindersCheckBox = new CheckBox("Enable maintenance reminders");
        enableRemindersCheckBox.setSelected(true);

        reminderDaysBeforeSpinner = new Spinner<>(1, 90, 30);
        reminderDaysBeforeSpinner.setEditable(true);
        reminderDaysBeforeSpinner.setPrefWidth(100);

        nextDueLabel = new Label();
        nextDueLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        // Create layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        int row = 0;

        // Truck info section
        Text truckInfo = new Text("🚛 " + truck.getDisplayName());
        truckInfo.setFont(Font.font("System", FontWeight.BOLD, 14));
        grid.add(truckInfo, 0, row++, 2, 1);

        Separator separator1 = new Separator();
        grid.add(separator1, 0, row++, 2, 1);

        // Last maintenance date
        Label dateLabel = new Label("Last Maintenance Date:");
        dateLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        grid.add(dateLabel, 0, row);
        grid.add(lastMaintenanceDatePicker, 1, row++);

        // Maintenance frequency
        Label frequencyLabel = new Label("Maintenance Interval:");
        frequencyLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        grid.add(frequencyLabel, 0, row);

        HBox frequencyBox = new HBox(10);
        frequencyBox.getChildren().addAll(
                frequencyComboBox,
                new Label("months"),
                new Label("OR"),
                customFrequencyField
        );
        frequencyBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(frequencyBox, 1, row++);

        // Quick select buttons
        HBox quickSelectBox = new HBox(10);
        Button btn3Months = new Button("3 Months");
        Button btn6Months = new Button("6 Months");
        Button btn12Months = new Button("1 Year");
        Button btn24Months = new Button("2 Years");

        btn3Months.setOnAction(e -> frequencyComboBox.setValue(3));
        btn6Months.setOnAction(e -> frequencyComboBox.setValue(6));
        btn12Months.setOnAction(e -> frequencyComboBox.setValue(12));
        btn24Months.setOnAction(e -> frequencyComboBox.setValue(24));

        quickSelectBox.getChildren().addAll(btn3Months, btn6Months, btn12Months, btn24Months);
        grid.add(new Label("Quick Select:"), 0, row);
        grid.add(quickSelectBox, 1, row++);

        Separator separator2 = new Separator();
        grid.add(separator2, 0, row++, 2, 1);

        // Reminder settings
        grid.add(enableRemindersCheckBox, 0, row++, 2, 1);

        Label reminderLabel = new Label("Remind me (days before due):");
        reminderLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        grid.add(reminderLabel, 0, row);
        grid.add(reminderDaysBeforeSpinner, 1, row++);

        reminderDaysBeforeSpinner.disableProperty().bind(enableRemindersCheckBox.selectedProperty().not());

        Separator separator3 = new Separator();
        grid.add(separator3, 0, row++, 2, 1);

        // Next due date display
        Label nextDueTitle = new Label("Next Maintenance Due:");
        nextDueTitle.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        grid.add(nextDueTitle, 0, row);
        grid.add(nextDueLabel, 1, row++);

        // Info box
        VBox infoBox = new VBox(5);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label infoTitle = new Label("💡 How Reminders Work:");
        infoTitle.setFont(Font.font("System", FontWeight.BOLD, 11));

        Label info1 = new Label("• Set when you last performed maintenance");
        Label info2 = new Label("• Choose how often maintenance is needed");
        Label info3 = new Label("• Get notified before the due date");
        Label info4 = new Label("• Overdue maintenance shows in RED in the table");

        info1.setFont(Font.font(10));
        info2.setFont(Font.font(10));
        info3.setFont(Font.font(10));
        info4.setFont(Font.font(10));

        infoBox.getChildren().addAll(infoTitle, info1, info2, info3, info4);
        grid.add(infoBox, 0, row++, 2, 1);

        getDialogPane().setContent(grid);

        // Buttons
        ButtonType saveButtonType = new ButtonType("Save & Enable", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Update next due date when inputs change
        lastMaintenanceDatePicker.valueProperty().addListener((obs, old, newVal) -> updateNextDueDate());
        frequencyComboBox.valueProperty().addListener((obs, old, newVal) -> updateNextDueDate());
        customFrequencyField.textProperty().addListener((obs, old, newVal) -> updateNextDueDate());

        // Initial calculation
        updateNextDueDate();

        // Convert result
        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                int interval = getSelectedInterval();
                if (interval <= 0) {
                    return null; // Invalid interval
                }

                MaintenanceResult result = new MaintenanceResult();
                result.lastMaintenanceDate = lastMaintenanceDatePicker.getValue();
                result.intervalMonths = interval;
                result.enableReminders = enableRemindersCheckBox.isSelected();
                result.reminderDaysBefore = reminderDaysBeforeSpinner.getValue();
                return result;
            }
            return null;
        });

        // Validation
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (lastMaintenanceDatePicker.getValue() == null) {
                showAlert("Please select a last maintenance date");
                event.consume();
            } else if (getSelectedInterval() <= 0) {
                showAlert("Please select or enter a valid maintenance interval");
                event.consume();
            }
        });
    }

    private void updateNextDueDate() {
        LocalDate lastDate = lastMaintenanceDatePicker.getValue();
        int interval = getSelectedInterval();

        if (lastDate != null && interval > 0) {
            LocalDate nextDue = lastDate.plusMonths(interval);
            long daysUntilDue = LocalDate.now().until(nextDue, java.time.temporal.ChronoUnit.DAYS);

            String dueText = nextDue.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"));

            if (daysUntilDue < 0) {
                nextDueLabel.setText("⚠️ " + dueText + " (OVERDUE by " + Math.abs(daysUntilDue) + " days)");
                nextDueLabel.setTextFill(Color.RED);
            } else if (daysUntilDue <= 30) {
                nextDueLabel.setText("⚠️ " + dueText + " (Due in " + daysUntilDue + " days)");
                nextDueLabel.setTextFill(Color.ORANGE);
            } else {
                nextDueLabel.setText("✓ " + dueText + " (" + daysUntilDue + " days away)");
                nextDueLabel.setTextFill(Color.GREEN);
            }
        } else {
            nextDueLabel.setText("Please set date and interval");
            nextDueLabel.setTextFill(Color.GRAY);
        }
    }

    private int getSelectedInterval() {
        // Check if custom field has value
        String customText = customFrequencyField.getText().trim();
        if (!customText.isEmpty()) {
            try {
                return Integer.parseInt(customText);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        // Otherwise use combo box
        Integer selected = frequencyComboBox.getValue();
        return selected != null ? selected : -1;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
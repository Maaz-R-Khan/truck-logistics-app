package org.example.trucklogisticsapp.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceRecord {

    private String recordId;
    private String truckId;
    private LocalDate scheduledDate;
    private LocalDate datePerformed;
    private String type;
    private String description;
    private String mechanicName;
    private double cost;
    private String status; // Scheduled, In Progress, Completed
    private LocalDate nextDueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MaintenanceRecord() {}

    public MaintenanceRecord(String recordId, String truckId, LocalDate scheduledDate, String type, double cost, String status) {
        this.recordId = recordId;
        this.truckId = truckId;
        this.scheduledDate = scheduledDate;
        this.type = type;
        this.cost = cost;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public boolean isOverdue() {
        return status.equalsIgnoreCase("Scheduled") && scheduledDate.isBefore(LocalDate.now());
    }

    public void markAsCompleted(LocalDate datePerformed) {
        this.datePerformed = datePerformed;
        this.status = "Completed";
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Maintenance [%s] - %s on Truck %s (%s)",
                recordId, type, truckId, status);
    }
}

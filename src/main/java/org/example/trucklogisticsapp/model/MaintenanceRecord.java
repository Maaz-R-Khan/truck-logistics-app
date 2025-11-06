package org.example.trucklogisticsapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceRecord {

    private String recordId;
    private String truckId;

    // To be consisntent with maaz changes
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

    public MaintenanceRecord() { }

    public MaintenanceRecord(
            String recordId,
            String truckId,
            LocalDate scheduledDate,
            String type,
            double cost,
            String status
    ) {
        this.recordId = recordId;
        this.truckId = truckId;
        this.scheduledDate = scheduledDate;
        this.type = type;
        this.cost = cost;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // ------------------ GETTERS ------------------ //

    public String getRecordId() { return recordId; }
    public String getTruckId() { return truckId; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getMechanicName() { return mechanicName; }
    public double getCost() { return cost; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public LocalDate getDatePerformed() { return datePerformed; }
    public LocalDate getNextDueDate() { return nextDueDate; }
    public String getStatus() { return status; }

    // ------------------ SETTERS ------------------ //

    public void setTruckId(String truckId) { this.truckId = truckId; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setMechanicName(String mechanicName) { this.mechanicName = mechanicName; }
    public void setDatePerformed(LocalDate datePerformed) { this.datePerformed = datePerformed; }
    public void setNextDueDate(LocalDate nextDueDate) { this.nextDueDate = nextDueDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCost(double cost) { this.cost = cost; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    // ------------------ LOGIC METHODS ------------------ //

    public boolean isOverdue() {
        return status.equalsIgnoreCase("Scheduled")
                && scheduledDate.isBefore(LocalDate.now());
    }

    public void markAsCompleted(LocalDate performedDate) {
        this.datePerformed = performedDate;
        this.status = "Completed";
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------ STRING ------------------ //

    @Override
    public String toString() {
        return String.format(
                "Maintenance [%s] - %s on Truck %s (%s)",
                recordId, type, truckId, status
        );
    }
}

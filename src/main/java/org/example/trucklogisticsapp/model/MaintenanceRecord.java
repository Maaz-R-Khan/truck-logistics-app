package org.example.trucklogisticsapp.model;

import java.time.LocalDate;

public class MaintenanceRecord {

    private String recordId;
    private String truckId;
    private String type;
    private String scheduledDate; // Stored as STRING for Firestore
    private double cost;
    private String status;

    // Firestore requires an empty constructor
    public MaintenanceRecord() {}

    public MaintenanceRecord(String recordId, String truckId, String type,
                             String scheduledDate, double cost, String status) {
        this.recordId = recordId;
        this.truckId = truckId;
        this.type = type;
        this.scheduledDate = scheduledDate;
        this.cost = cost;
        this.status = status;
    }

    // GETTERS
    public String getRecordId() { return recordId; }
    public String getTruckId() { return truckId; }
    public String getType() { return type; }
    public String getScheduledDate() { return scheduledDate; }
    public double getCost() { return cost; }
    public String getStatus() { return status; }

    // SETTERS
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public void setTruckId(String truckId) { this.truckId = truckId; }
    public void setType(String type) { this.type = type; }
    public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }
    public void setCost(double cost) { this.cost = cost; }
    public void setStatus(String status) { this.status = status; }

    // Convert stored String → LocalDate
    public LocalDate toLocalDate() {
        try {
            return (scheduledDate != null && !scheduledDate.isEmpty())
                    ? LocalDate.parse(scheduledDate)
                    : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Convert LocalDate → stored String
    public void setScheduledDateFromLocalDate(LocalDate date) {
        this.scheduledDate = (date != null) ? date.toString() : null;
    }
}

package org.example.trucklogisticsapp.model;

import java.time.LocalDate;

/**
 * Result object returned from MaintenanceDialog
 * Contains the maintenance settings chosen by the user
 */
public class MaintenanceResult {

    public LocalDate lastMaintenanceDate;
    public int intervalMonths;
    public boolean enableReminders;
    public int reminderDaysBefore;

    /**
     * Default constructor
     */
    public MaintenanceResult() {
    }

    /**
     * Full constructor
     */
    public MaintenanceResult(LocalDate lastMaintenanceDate, int intervalMonths,
                             boolean enableReminders, int reminderDaysBefore) {
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.intervalMonths = intervalMonths;
        this.enableReminders = enableReminders;
        this.reminderDaysBefore = reminderDaysBefore;
    }

    @Override
    public String toString() {
        return "MaintenanceResult{" +
                "lastMaintenanceDate=" + lastMaintenanceDate +
                ", intervalMonths=" + intervalMonths +
                ", enableReminders=" + enableReminders +
                ", reminderDaysBefore=" + reminderDaysBefore +
                '}';
    }
}
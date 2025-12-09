package org.example.trucklogisticsapp.model;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.firestore.annotation.Exclude;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Truck model with comprehensive maintenance tracking
 * Includes automatic reminder calculations for scheduled maintenance
 * FIRESTORE COMPATIBLE - Uses @PropertyName for field mapping
 */
public class Truck {

    // Basic truck information
    @PropertyName("truckId")
    private String id;

    private String vin;
    private String make;
    private String model;
    private int year;
    private double mileage;

    @PropertyName("capacityLbs")
    private int capacityKg;

    @PropertyName("liscensePlate")
    private String plateNumber;

    private String source;
    private String notes;

    // Status fields
    private boolean available = true;
    private boolean needsMaintenance = false;

    // Additional Firestore fields
    @PropertyName("status")
    private String status;

    @PropertyName("currentDriver")
    private String currentDriver;

    @PropertyName("fuelMpg")
    private Double fuelMpg;

    // Maintenance tracking fields
    private LocalDate lastMaintenanceDate;
    private int maintenanceIntervalMonths = 6; // Default 6 months

    // ========================================
    // CONSTRUCTORS
    // ========================================

    /**
     * Full constructor
     */
    public Truck(String id, String vin, String make, String model, int year,
                 double mileage, int capacityKg, String plateNumber) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.capacityKg = capacityKg;
        this.plateNumber = plateNumber;
        this.available = true;
        this.needsMaintenance = false;
        this.notes = "";
        this.source = "";
    }

    /**
     * Simple constructor (most commonly used)
     */
    public Truck(String vin, String make, String model, int year, int capacityKg) {
        this(UUID.randomUUID().toString(), vin, make, model, year, 0, capacityKg, "");
    }

    /**
     * Default constructor - REQUIRED for Firestore
     */
    public Truck() {
        this.id = UUID.randomUUID().toString();
        this.available = true;
        this.needsMaintenance = false;
        this.notes = "";
        this.source = "";
    }

    // ========================================
    // BASIC GETTERS AND SETTERS
    // ========================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Firestore mapping for truckId
    @PropertyName("truckId")
    public String getTruckId() {
        return id;
    }

    @PropertyName("truckId")
    public void setTruckId(String id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public int getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(int capacityKg) {
        this.capacityKg = capacityKg;
    }

    // Firestore mapping for capacityLbs
    @PropertyName("capacityLbs")
    public int getCapacityLbs() {
        return capacityKg;
    }

    @PropertyName("capacityLbs")
    public void setCapacityLbs(int capacityKg) {
        this.capacityKg = capacityKg;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    // Firestore mapping for liscensePlate (typo in Firestore)
    @PropertyName("liscensePlate")
    public String getLiscensePlate() {
        return plateNumber;
    }

    @PropertyName("liscensePlate")
    public void setLiscensePlate(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    // NEW: Source getter/setter
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isNeedsMaintenance() {
        return needsMaintenance;
    }

    public void setNeedsMaintenance(boolean needsMaintenance) {
        this.needsMaintenance = needsMaintenance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Additional Firestore fields

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(String currentDriver) {
        this.currentDriver = currentDriver;
    }

    public Double getFuelMpg() {
        return fuelMpg;
    }

    public void setFuelMpg(Double fuelMpg) {
        this.fuelMpg = fuelMpg;
    }

    // ========================================
    // MAINTENANCE TRACKING - GETTERS/SETTERS
    // ========================================

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public int getMaintenanceIntervalMonths() {
        return maintenanceIntervalMonths;
    }

    public void setMaintenanceIntervalMonths(int maintenanceIntervalMonths) {
        this.maintenanceIntervalMonths = maintenanceIntervalMonths;
    }

    // ========================================
    // MAINTENANCE TRACKING - CALCULATED METHODS
    // Mark with @Exclude so Firestore doesn't try to deserialize them
    // ========================================

    /**
     * Calculates when the next maintenance is due
     * @return LocalDate of next maintenance, or null if not configured
     */
    @Exclude
    public LocalDate getNextMaintenanceDue() {
        if (lastMaintenanceDate == null || maintenanceIntervalMonths <= 0) {
            return null;
        }
        return lastMaintenanceDate.plusMonths(maintenanceIntervalMonths);
    }

    /**
     * Gets a human-readable maintenance status string with color indicators
     * @return Status string (e.g., "OK", "Due Soon", "OVERDUE")
     */
    @Exclude
    public String getMaintenanceStatus() {
        LocalDate nextDue = getNextMaintenanceDue();

        if (nextDue == null) {
            return "Not configured";
        }

        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), nextDue);

        if (daysUntil < 0) {
            // Overdue - RED
            return "OVERDUE";
        } else if (daysUntil <= 30) {
            // Due soon (within 30 days) - ORANGE
            return "Due Soon";
        } else {
            // All good - GREEN
            return "OK";
        }
    }

    /**
     * Checks if maintenance is currently overdue
     * @return true if overdue, false otherwise
     */
    @Exclude
    public boolean isMaintenanceOverdue() {
        LocalDate nextDue = getNextMaintenanceDue();
        if (nextDue == null) {
            return false;
        }
        return LocalDate.now().isAfter(nextDue);
    }

    /**
     * Checks if maintenance is due soon (within 30 days)
     * @return true if due soon, false otherwise
     */
    @Exclude
    public boolean isMaintenanceDueSoon() {
        LocalDate nextDue = getNextMaintenanceDue();
        if (nextDue == null) {
            return false;
        }
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), nextDue);
        return daysUntil >= 0 && daysUntil <= 30;
    }

    /**
     * Gets the number of days until next maintenance
     * Negative number means overdue
     * @return days until maintenance (negative if overdue, Long.MAX_VALUE if not configured)
     */
    @Exclude
    public long getDaysUntilMaintenance() {
        LocalDate nextDue = getNextMaintenanceDue();
        if (nextDue == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), nextDue);
    }

    /**
     * Marks maintenance as completed (sets last maintenance to today)
     */
    public void markMaintenanceCompleted() {
        this.lastMaintenanceDate = LocalDate.now();
        this.needsMaintenance = false;
    }


    /**
     * Gets a display-friendly name for the truck
     * @return String like "Volvo FH16 (VIN123)" or "Truck"
     */
    @Exclude
    public String getDisplayName() {
        String mk = (make == null || make.isBlank()) ? "Truck" : make;
        String md = (model == null || model.isBlank()) ? "" : (" " + model);
        return mk + md + (vin == null || vin.isBlank() ? "" : " (" + vin + ")");
    }

    /**
     * Validates if the truck has required information
     */
    @Exclude
    public boolean isValid() {
        return vin != null && !vin.isBlank() &&
                make != null && !make.isBlank() &&
                model != null && !model.isBlank() &&
                year > 1900 && year <= LocalDate.now().getYear() + 1;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "id='" + id + '\'' +
                ", vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", mileage=" + mileage +
                ", capacityKg=" + capacityKg +
                ", plateNumber='" + plateNumber + '\'' +
                ", source='" + source + '\'' +
                ", available=" + available +
                ", needsMaintenance=" + needsMaintenance +
                ", maintenanceStatus='" + getMaintenanceStatus() + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truck)) return false;
        Truck truck = (Truck) o;
        return Objects.equals(vin, truck.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }
}
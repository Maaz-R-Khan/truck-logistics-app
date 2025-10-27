package org.example.trucklogisticsapp.model;

import java.util.Objects;
import java.util.UUID;

public class Truck {

    private String id;              // UUID
    private String vin;             // Vehicle Identification Number (unique)
    private String make;            // e.g., Volvo, Scania
    private String model;           // e.g., FH16, R450
    private int year;               // model year

    private double mileage;         // in kilometers or miles
    private double capacityKg;      // payload capacity in kg
    private String plateNumber;     // license plate

    private boolean available = true;
    private boolean needsMaintenance = false;

    private String photoPath;       // optional: image on disk
    private String notes;           // free-form notes

    private String createdAt;       // ISO-8601 timestamp string (e.g., OffsetDateTime.now().toString())


    public Truck() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = java.time.OffsetDateTime.now().toString();
    }

    /** Convenience constructor for required fields. */
    public Truck(String vin, String make, String model, int year, double capacityKg) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = java.time.OffsetDateTime.now().toString();

        this.vin = safeStr(vin);
        this.make = safeStr(make);
        this.model = safeStr(model);
        this.year = year;
        this.capacityKg = capacityKg;

        // defaults
        this.mileage = 0.0;
        this.available = true;
        this.needsMaintenance = false;
        this.plateNumber = "";
        this.photoPath = "";
        this.notes = "";
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = safeStr(id);
    }

    public String getVin() {
        return vin;
    }
    public void setVin(String vin) {
        this.vin = safeStr(vin);
    }

    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = safeStr(make);
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = safeStr(model);
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
        this.mileage = Math.max(0.0, mileage);
    }

    public double getCapacityKg() {
        return capacityKg;
    }
    public void setCapacityKg(double capacityKg) {
        this.capacityKg = Math.max(0.0, capacityKg);
    }

    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = safeStr(plateNumber);
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

    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = safeStr(photoPath);
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = safeStr(notes);
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = safeStr(createdAt);
    }


    /** Increase mileage by a positive amount. */
    public void addMileage(double delta) {
        if (delta > 0) this.mileage += delta;
    }

    /** Mark truck as due for maintenance (e.g., after threshold). */
    public void markMaintenanceDue() {
        this.needsMaintenance = true;
        this.available = false; // usually not available while maintenance is due
    }

    /** Mark maintenance completed and truck available again. */
    public void markMaintenanceCompleted() {
        this.needsMaintenance = false;
        this.available = true;
    }

    /** Optional display-friendly name. */
    public String getDisplayName() {
        String mk = (make == null || make.isBlank()) ? "Truck" : make;
        String md = (model == null || model.isBlank()) ? "" : (" " + model);
        return mk + md + (vin == null || vin.isBlank() ? "" : " (" + vin + ")");
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
                ", available=" + available +
                ", needsMaintenance=" + needsMaintenance +
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


    private static String safeStr(String s) {
        return (s == null) ? "" : s.trim();
    }
}


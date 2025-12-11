package org.example.trucklogisticsapp.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Driver model - Firestore-compatible (all date fields stored as String)
 */
public class Driver {

    // Basic Information
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth; // "YYYY-MM-DD"
    private String address;
    private String city;
    private String state;
    private String zipCode;

    // License Information
    private String licenseNumber;
    private String licenseState;
    private String licenseClass;
    private String licenseExpiry; // String

    // Medical Certification
    private String medicalCertExpiry; // String

    // Employment Information
    private String hireDate; // String
    private String status;
    private boolean available;

    // Assignment
    private String assignedTruckId;

    // Endorsements
    private boolean hazmatEndorsement;
    private boolean tankersEndorsement;
    private boolean doublesEndorsement;

    // Performance
    private double rating;
    private int totalTrips;
    private double totalMiles;
    private String notes;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public Driver() {
        this.id = UUID.randomUUID().toString();
        this.status = "Active";
        this.available = true;
        this.licenseClass = "Class A";
        this.rating = 5.0;
        this.totalTrips = 0;
        this.totalMiles = 0.0;
        this.notes = "";
    }

    public Driver(String firstName, String lastName, String licenseNumber,
                  String licenseState, String phone, String email) {

        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
        this.licenseState = licenseState;
        this.phone = phone;
        this.email = email;
    }

    public Driver(String firstName, String lastName, String licenseNumber) {
        this(firstName, lastName, licenseNumber, "", "", "");
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getLicenseState() { return licenseState; }
    public void setLicenseState(String licenseState) { this.licenseState = licenseState; }

    public String getLicenseClass() { return licenseClass; }
    public void setLicenseClass(String licenseClass) { this.licenseClass = licenseClass; }

    public String getLicenseExpiry() { return licenseExpiry; }
    public void setLicenseExpiry(String licenseExpiry) { this.licenseExpiry = licenseExpiry; }

    public String getMedicalCertExpiry() { return medicalCertExpiry; }
    public void setMedicalCertExpiry(String medicalCertExpiry) { this.medicalCertExpiry = medicalCertExpiry; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getAssignedTruckId() { return assignedTruckId; }
    public void setAssignedTruckId(String assignedTruckId) { this.assignedTruckId = assignedTruckId; }

    public boolean isHazmatEndorsement() { return hazmatEndorsement; }
    public void setHazmatEndorsement(boolean hazmatEndorsement) { this.hazmatEndorsement = hazmatEndorsement; }

    public boolean isTankersEndorsement() { return tankersEndorsement; }
    public void setTankersEndorsement(boolean tankersEndorsement) { this.tankersEndorsement = tankersEndorsement; }

    public boolean isDoublesEndorsement() { return doublesEndorsement; }
    public void setDoublesEndorsement(boolean doublesEndorsement) { this.doublesEndorsement = doublesEndorsement; }

    public double getRating() { return rating; }
    public void setRating(double rating) {
        this.rating = Math.max(0.0, Math.min(5.0, rating));
    }

    public int getTotalTrips() { return totalTrips; }
    public void setTotalTrips(int totalTrips) { this.totalTrips = totalTrips; }

    public double getTotalMiles() { return totalMiles; }
    public void setTotalMiles(double totalMiles) { this.totalMiles = totalMiles; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // =========================================================================
    // UTILITY METHODS
    // =========================================================================

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFormattedAddress() {
        if (address == null || address.isEmpty()) return "N/A";

        StringBuilder sb = new StringBuilder(address);
        if (city != null && !city.isEmpty()) sb.append(", ").append(city);
        if (state != null && !state.isEmpty()) sb.append(", ").append(state);
        if (zipCode != null && !zipCode.isEmpty()) sb.append(" ").append(zipCode);
        return sb.toString();
    }

    public String getEndorsements() {
        StringBuilder sb = new StringBuilder();
        if (hazmatEndorsement) sb.append("HazMat, ");
        if (tankersEndorsement) sb.append("Tankers, ");
        if (doublesEndorsement) sb.append("Doubles, ");

        if (sb.length() > 0) return sb.substring(0, sb.length() - 2);
        return "None";
    }

    public String getComplianceStatus() {
        LocalDate lic = stringToDate(licenseExpiry);
        LocalDate med = stringToDate(medicalCertExpiry);

        boolean licValid = lic != null && LocalDate.now().isBefore(lic);
        boolean medValid = med != null && LocalDate.now().isBefore(med);

        if (licValid && medValid) {
            long daysLic = ChronoUnit.DAYS.between(LocalDate.now(), lic);
            long daysMed = ChronoUnit.DAYS.between(LocalDate.now(), med);
            if (daysLic <= 30 || daysMed <= 30) return "Expiring Soon";
            return "Compliant";
        } else if (!licValid && !medValid) return "Non-Compliant";
        else if (!licValid) return "License Expired";
        else return "Medical Expired";
    }

    private LocalDate stringToDate(String date) {
        try {
            return (date == null || date.isEmpty()) ? null : LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + getFullName() + '\'' +
                ", license='" + licenseNumber + '\'' +
                ", status='" + status + '\'' +
                ", available=" + available +
                ", compliance='" + getComplianceStatus() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
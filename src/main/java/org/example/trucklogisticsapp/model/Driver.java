package org.example.trucklogisticsapp.model;

import java.util.UUID;

public class Driver {

    private String id;
    private String name;

    private String licenseNumber;
    private String licenseExpiry;

    private String phone;
    private String address;

    private String photoPath;
    private String resumePath;

    private String backgroundNotes;

    private boolean active = true;
    private String createdAt;

    public Driver() {

    }

    public Driver(String licenseNumber, String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.createdAt = java.time.OffsetDateTime.now().toString();
        this.licenseExpiry = "";
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = (name == null ? "" : name.trim());
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseExpiry() {
        return licenseExpiry;
    }
    public void setLicenseExpiry(String licenseExpiry) {
        this.licenseExpiry = (licenseExpiry == null ? "" : licenseExpiry);
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getResumePath() {
        return resumePath;
    }
    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public String getBackgroundNotes() {
        return backgroundNotes;
    }
    public void setBackgroundNotes(String backgroundNotes) {
        this.backgroundNotes = backgroundNotes;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}

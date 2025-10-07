package org.example.trucklogisticsapp.model;

import java.util.UUID;

public class Driver {

    private String id;
    private String name;

    private String licenseNumber;
    private String licenseExpiry;

    private String phone;
    private String Address;

    private String photoPath;
    private String resumePath;

    private String backgroundNotes;

    private boolean active = true;
    private String createdAt;

    public Driver() {

    }

    public Driver(String id, String licenseNumber){
        this.id = UUID.randomUUID().toString();
    }
}

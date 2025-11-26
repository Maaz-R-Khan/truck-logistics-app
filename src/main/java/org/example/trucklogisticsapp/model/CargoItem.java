package org.example.trucklogisticsapp.model;

import java.util.UUID;

public class CargoItem {

    private String itemId;
    private String name;
    private String description;

    private int quantity;
    private double price;
    private double weight;

    public CargoItem() {}

    public CargoItem(String id, String name) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
    }

    public CargoItem(String id, String name, String description, int quantity, double price, double weight) {
        this.itemId = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;}
    public String getDescription() {return description;}

    public void setId(String id) {this.itemId=id;}
    public String getId() {return itemId;}

    public void setName(String name) {this.name=name;}
    public String getName() {return name;}

    public void setQuantity(int quantity) {this.quantity=quantity;}
    public int getQuantity() {return quantity;}

    public void setPrice(double price) {this.price=price;}
    public double getPrice() {return price * quantity;}

    public void setWeight(double weight) {this.weight=weight;}
    public double getWeight() {return weight * quantity;}
}

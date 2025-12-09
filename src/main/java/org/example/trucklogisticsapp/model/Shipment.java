package org.example.trucklogisticsapp.model;

import com.google.cloud.firestore.annotation.PropertyName;
import javafx.beans.property.*;

public class Shipment {

    private final StringProperty shipmentId = new SimpleStringProperty();
    private final StringProperty route = new SimpleStringProperty();
    private final StringProperty customer = new SimpleStringProperty();
    private final StringProperty weight = new SimpleStringProperty();
    private final IntegerProperty value = new SimpleIntegerProperty();
    private final StringProperty priority = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty assignment = new SimpleStringProperty();
    private final StringProperty delivery = new SimpleStringProperty();

    // Required for Firestore
    public Shipment() {}

    // ========== FIRESTORE SAFE SETTERS ==========
    @PropertyName("value")
    public void setValueFromFirestore(Object v) {
        if (v == null) {
            this.value.set(0);
        } else if (v instanceof Number) {
            this.value.set(((Number) v).intValue());
        } else {
            try {
                this.value.set(Integer.parseInt(v.toString()));
            } catch (Exception e) {
                this.value.set(0);
            }
        }
    }

    @PropertyName("weight")
    public void setWeightFromFirestore(Object w) {
        if (w == null) {
            this.weight.set("");
        } else {
            this.weight.set(w.toString());
        }
    }

    // Constructor for manual creation
    public Shipment(String shipmentId,
                    String route,
                    String customer,
                    String weight,
                    int value,
                    String priority,
                    String status,
                    String assignment,
                    String delivery) {

        this.shipmentId.set(shipmentId);
        this.route.set(route);
        this.customer.set(customer);
        this.weight.set(weight);
        this.value.set(value);
        this.priority.set(priority);
        this.status.set(status);
        this.assignment.set(assignment);
        this.delivery.set(delivery);
    }

    // GETTERS
    public String getShipmentId() { return shipmentId.get(); }
    public String getRoute() { return route.get(); }
    public String getCustomer() { return customer.get(); }
    public String getWeight() { return weight.get(); }
    public int getValue() { return value.get(); }
    public String getPriority() { return priority.get(); }
    public String getStatus() { return status.get(); }
    public String getAssignment() { return assignment.get(); }
    public String getDelivery() { return delivery.get(); }

    // SETTERS
    public void setShipmentId(String shipmentId) { this.shipmentId.set(shipmentId); }
    public void setRoute(String route) { this.route.set(route); }
    public void setCustomer(String customer) { this.customer.set(customer); }
    public void setWeight(String weight) { this.weight.set(weight); }
    public void setValue(int value) { this.value.set(value); }
    public void setPriority(String priority) { this.priority.set(priority); }
    public void setStatus(String status) { this.status.set(status); }
    public void setAssignment(String assignment) { this.assignment.set(assignment); }
    public void setDelivery(String delivery) { this.delivery.set(delivery); }

    // PROPERTY GETTERS
    public StringProperty shipmentIdProperty() { return shipmentId; }
    public StringProperty routeProperty() { return route; }
    public StringProperty customerProperty() { return customer; }
    public StringProperty weightProperty() { return weight; }
    public IntegerProperty valueProperty() { return value; }
    public StringProperty priorityProperty() { return priority; }
    public StringProperty statusProperty() { return status; }
    public StringProperty assignmentProperty() { return assignment; }
    public StringProperty deliveryProperty() { return delivery; }
}

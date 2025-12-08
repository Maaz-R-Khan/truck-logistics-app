package org.example.trucklogisticsapp.model;

import com.google.cloud.firestore.annotation.PropertyName;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Shipment {

    private final StringProperty shipmentId = new SimpleStringProperty();
    private final StringProperty route = new SimpleStringProperty();
    private final StringProperty customer = new SimpleStringProperty();
    private final StringProperty weight = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();
    private final StringProperty priority = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty assignment = new SimpleStringProperty();
    private final StringProperty delivery = new SimpleStringProperty();

    // Required no-arg constructor
    public Shipment() {}

    // ========= Firestore Safe Setter for "value" ===========
    @PropertyName("value")
    public void setValueFromFirestore(Object v) {
        if (v == null) {
            this.value.set("");
        } else if (v instanceof Number) {
            this.value.set(String.valueOf(((Number) v).doubleValue()));
        } else {
            this.value.set(String.valueOf(v));
        }
    }

    // ========= Firestore Safe Setter for "weight" ===========
    @PropertyName("weight")
    public void setWeightFromFirestore(Object w) {
        if (w == null) {
            this.weight.set("");
        } else if (w instanceof Number) {
            this.weight.set(String.valueOf(((Number) w).doubleValue()));
        } else {
            this.weight.set(String.valueOf(w));
        }
    }

    // ========= Manual constructor for creating shipments ===========
    public Shipment(String shipmentId,
                    String route,
                    String customer,
                    String weight,
                    String value,
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

    // ======== GETTERS ==========
    public String getShipmentId() { return shipmentId.get(); }
    public String getRoute() { return route.get(); }
    public String getCustomer() { return customer.get(); }
    public String getWeight() { return weight.get(); }
    public String getValue() { return value.get(); }
    public String getPriority() { return priority.get(); }
    public String getStatus() { return status.get(); }
    public String getAssignment() { return assignment.get(); }
    public String getDelivery() { return delivery.get(); }

    // ======== SETTERS ==========
    public void setShipmentId(String shipmentId) { this.shipmentId.set(shipmentId); }
    public void setRoute(String route) { this.route.set(route); }
    public void setCustomer(String customer) { this.customer.set(customer); }
    public void setPriority(String priority) { this.priority.set(priority); }
    public void setStatus(String status) { this.status.set(status); }
    public void setAssignment(String assignment) { this.assignment.set(assignment); }
    public void setDelivery(String delivery) { this.delivery.set(delivery); }

    // ======== PROPERTY GETTERS (JavaFX Bindings) ==========
    public StringProperty shipmentIdProperty() { return shipmentId; }
    public StringProperty routeProperty() { return route; }
    public StringProperty customerProperty() { return customer; }
    public StringProperty weightProperty() { return weight; }
    public StringProperty valueProperty() { return value; }
    public StringProperty priorityProperty() { return priority; }
    public StringProperty statusProperty() { return status; }
    public StringProperty assignmentProperty() { return assignment; }
    public StringProperty deliveryProperty() { return delivery; }

}

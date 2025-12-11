package org.example.trucklogisticsapp.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
// imports to add at top
import javafx.beans.value.ObservableValue;


public class Shipment {


    private StringProperty shipmentId = new SimpleStringProperty();
    private StringProperty route = new SimpleStringProperty();
    private StringProperty customer = new SimpleStringProperty();
    private StringProperty weight = new SimpleStringProperty();
    private IntegerProperty value = new SimpleIntegerProperty();
    private StringProperty priority = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty();
    private StringProperty assignment = new SimpleStringProperty();
    private StringProperty delivery = new SimpleStringProperty();

    public Shipment() {}

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

    public String getShipmentId() { return shipmentId.get(); }
    public String getRoute() { return route.get(); }
    public String getCustomer() { return customer.get(); }
    public String getWeight() { return weight.get(); }
    public int getValue() { return value.get(); }
    public String getPriority() { return priority.get(); }
    public String getStatus() { return status.get(); }
    public String getAssignment() { return assignment.get(); }
    public String getDelivery() { return delivery.get(); }

    public void setShipmentId(String shipmentId) { this.shipmentId.set(shipmentId); }
    public void setRoute(String route) { this.route.set(route); }
    public void setCustomer(String customer) { this.customer.set(customer); }
    public void setWeight(String weight) { this.weight.set(weight); }
    public void setValue(int value) { this.value.set(value); }
    public void setPriority(String priority) { this.priority.set(priority); }
    public void setStatus(String status) { this.status.set(status); }
    public void setAssignment(String assignment) { this.assignment.set(assignment); }
    public void setDelivery(String delivery) { this.delivery.set(delivery); }

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

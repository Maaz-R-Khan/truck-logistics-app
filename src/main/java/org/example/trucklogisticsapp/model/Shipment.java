package org.example.trucklogisticsapp.model;

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

    public String getShipmentId() { return shipmentId.get(); }
    public StringProperty shipmentIdProperty() { return shipmentId; }

    public String getRoute() { return route.get(); }
    public StringProperty routeProperty() { return route; }

    public String getCustomer() { return customer.get(); }
    public StringProperty customerProperty() { return customer; }

    public String getWeight() { return weight.get(); }
    public StringProperty weightProperty() { return weight; }

    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }

    public String getPriority() { return priority.get(); }
    public StringProperty priorityProperty() { return priority; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public String getAssignment() { return assignment.get(); }
    public StringProperty assignmentProperty() { return assignment; }

    public String getDelivery() { return delivery.get(); }
    public StringProperty deliveryProperty() { return delivery; }
}

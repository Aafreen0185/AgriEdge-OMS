package com.agriedge;

public class Shipment {
    private String id;
    private String orderId;
    private String trackingNumber;
    private String carrier;     // FedEx, UPS, DHL, Local Courier
    private String status;      // Pending, In Transit, Delivered

    public Shipment(String id, String orderId, String trackingNumber, String carrier, String status) {
        this.id = id;
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Shipment{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", carrier='" + carrier + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

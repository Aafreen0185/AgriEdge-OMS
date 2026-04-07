package com.agriedge;

import java.util.*;

public class ShipmentService {
    private static final List<Shipment> shipments = new ArrayList<>();

    /**
     * Creates a new shipment for an order
     */
    public static Shipment createShipment(String orderId) {
        String id = UUID.randomUUID().toString();
        String trackingNumber = "TEST_" + orderId;
        Shipment shipment = new Shipment(id, orderId, trackingNumber, "Local Courier", "Pending");
        shipments.add(shipment);
        System.out.println("✓ Shipment created: Tracking " + trackingNumber);
        return shipment;
    }

    /**
     * Updates shipment status
     */
    public static void updateShipmentStatus(String shipmentId, String newStatus) {
        if (!ValidationService.validateShipmentStatus(newStatus)) {
            System.out.println("✗ Invalid shipment status: " + newStatus);
            return;
        }

        Shipment shipment = getShipmentById(shipmentId);
        if (shipment != null) {
            // Validate tracking number for Delivered status
            if ("Delivered".equals(newStatus)) {
                if (!ValidationService.validateTrackingNumberForDelivered(shipment.getTrackingNumber(), "Delivered")) {
                    System.out.println("✗ Validation Error: Tracking number is required before marking shipment as Delivered");
                    return;
                }
            }
            
            String oldStatus = shipment.getStatus();
            shipment.setStatus(newStatus);
            System.out.println("✓ Shipment status updated: " + oldStatus + " → " + newStatus);
        } else {
            System.out.println("✗ Shipment not found with ID: " + shipmentId);
        }
    }

    /**
     * Updates tracking number for a shipment
     */
    public static void updateTrackingNumber(String shipmentId, String trackingNumber) {
        Shipment shipment = getShipmentById(shipmentId);
        if (shipment != null) {
            shipment.setTrackingNumber(trackingNumber);
            System.out.println("✓ Tracking number updated: " + trackingNumber);
        } else {
            System.out.println("✗ Shipment not found with ID: " + shipmentId);
        }
    }

    /**
     * Gets shipment by ID
     */
    public static Shipment getShipmentById(String shipmentId) {
        return shipments.stream()
                .filter(s -> s.getId().equals(shipmentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets shipment by order ID
     */
    public static Shipment getShipmentByOrderId(String orderId) {
        return shipments.stream()
                .filter(s -> s.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all shipments
     */
    public static List<Shipment> getAllShipments() {
        return new ArrayList<>(shipments);
    }

    /**
     * Deletes a shipment by ID
     */
    public static void deleteShipment(String shipmentId) {
        Shipment shipment = getShipmentById(shipmentId);
        if (shipment != null) {
            shipments.remove(shipment);
            System.out.println("✓ Shipment deleted");
        }
    }

    /**
     * Deletes shipments for a specific order
     */
    public static void deleteShipmentsByOrderId(String orderId) {
        List<Shipment> toRemove = new ArrayList<>();
        for (Shipment s : shipments) {
            if (s.getOrderId().equals(orderId)) {
                toRemove.add(s);
            }
        }
        shipments.removeAll(toRemove);
        if (!toRemove.isEmpty()) {
            System.out.println("✓ " + toRemove.size() + " shipment(s) deleted for order");
        }
    }
}

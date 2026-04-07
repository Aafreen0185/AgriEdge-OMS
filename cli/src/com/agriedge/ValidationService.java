package com.agriedge;

public class ValidationService {
    
    /**
     * Validates that tracking number exists before marking shipment as Delivered
     */
    public static boolean validateTrackingNumberForDelivered(String trackingNumber, String orderStatus) {
        if ("Delivered".equals(orderStatus)) {
            return trackingNumber != null && !trackingNumber.trim().isEmpty();
        }
        return true;
    }

    /**
     * Validates that inventory stock level does not fall below reorder level
     */
    public static boolean validateInventoryLevel(int stockQuantity, int reorderLevel) {
        return stockQuantity > reorderLevel;
    }

    /**
     * Validates payment status is valid
     */
    public static boolean validatePaymentStatus(String paymentStatus) {
        return paymentStatus != null && 
               (paymentStatus.equals("Pending") || paymentStatus.equals("Paid") || paymentStatus.equals("Failed"));
    }

    /**
     * Validates order status is valid
     */
    public static boolean validateOrderStatus(String orderStatus) {
        return orderStatus != null && 
               (orderStatus.equals("New") || orderStatus.equals("Processing") || 
                orderStatus.equals("Shipped") || orderStatus.equals("Delivered") || orderStatus.equals("Canceled"));
    }

    /**
     * Validates shipment status is valid
     */
    public static boolean validateShipmentStatus(String shipmentStatus) {
        return shipmentStatus != null && 
               (shipmentStatus.equals("Pending") || shipmentStatus.equals("In Transit") || shipmentStatus.equals("Delivered"));
    }
}

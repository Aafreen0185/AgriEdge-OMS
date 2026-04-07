package com.agriedge;

public class EmailNotifier {
    
    /**
     * Simulates sending an email notification when payment status changes to Paid
     */
    public static void sendOrderPaidNotification(Order order) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("EMAIL NOTIFICATION SENT");
        System.out.println("=".repeat(70));
        System.out.println("To: " + order.getCustomerName() + "@agriedge.com");
        System.out.println("Subject: Order " + order.getOrderNumber() + " Payment Confirmed");
        System.out.println("-".repeat(70));
        System.out.println("Dear " + order.getCustomerName() + ",");
        System.out.println();
        System.out.println("Your order has been received and payment confirmed!");
        System.out.println();
        System.out.println("Order Details:");
        System.out.println("  Order Number: " + order.getOrderNumber());
        System.out.println("  Status: " + order.getOrderStatus());
        System.out.println("  Total Amount: $" + String.format("%.2f", order.getTotalAmount()));
        System.out.println("  Payment Status: " + order.getPaymentStatus());
        System.out.println("  Shipping Address: " + order.getShippingAddress());
        System.out.println("  Discounted Total: $" + String.format("%.2f", order.getTotalAmount() * 0.9));
        System.out.println();
        System.out.println("Your order will be shipped shortly. Thank you for your business!");
        System.out.println();
        System.out.println("Best regards,");
        System.out.println("AgriEdge Or-Mange Ltd");
        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Simulates sending a cancellation notice when payment fails
     */
    public static void sendOrderCancelledNotification(Order order) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("EMAIL NOTIFICATION SENT");
        System.out.println("=".repeat(70));
        System.out.println("To: " + order.getCustomerName() + "@agriedge.com");
        System.out.println("Subject: Order " + order.getOrderNumber() + " Cancelled - Payment Failed");
        System.out.println("-".repeat(70));
        System.out.println("Dear " + order.getCustomerName() + ",");
        System.out.println();
        System.out.println("Unfortunately, your order has been cancelled due to payment failure.");
        System.out.println();
        System.out.println("Order Details:");
        System.out.println("  Order Number: " + order.getOrderNumber());
        System.out.println("  Status: " + order.getOrderStatus());
        System.out.println("  Payment Status: " + order.getPaymentStatus());
        System.out.println();
        System.out.println("Please update your payment method and try again.");
        System.out.println();
        System.out.println("Best regards,");
        System.out.println("AgriEdge Or-Mange Ltd");
        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Simulates sending a shipment update notification
     */
    public static void sendShipmentNotification(Order order, Shipment shipment) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("EMAIL NOTIFICATION SENT");
        System.out.println("=".repeat(70));
        System.out.println("To: " + order.getCustomerName() + "@agriedge.com");
        System.out.println("Subject: Shipment Update for Order " + order.getOrderNumber());
        System.out.println("-".repeat(70));
        System.out.println("Dear " + order.getCustomerName() + ",");
        System.out.println();
        System.out.println("Your order is on its way!");
        System.out.println();
        System.out.println("Shipment Details:");
        System.out.println("  Order Number: " + order.getOrderNumber());
        System.out.println("  Tracking Number: " + shipment.getTrackingNumber());
        System.out.println("  Carrier: " + shipment.getCarrier());
        System.out.println("  Status: " + shipment.getStatus());
        System.out.println();
        System.out.println("You can track your shipment using the tracking number above.");
        System.out.println();
        System.out.println("Best regards,");
        System.out.println("AgriEdge Or-Mange Ltd");
        System.out.println("=".repeat(70) + "\n");
    }
}

package com.agriedge;

import java.util.*;

public class OrderService {
    private static final List<Order> orders = new ArrayList<>();
    private static int orderCounter = 1;

    /**
     * Creates a new order with auto-generated order number
     */
    public static Order createOrder(String customerName, String shippingAddress) {
        String orderNumber = String.format("ORD-%04d", orderCounter++);
        String id = UUID.randomUUID().toString();
        Order order = new Order(id, orderNumber, customerName, "New", "Pending", 0.0, shippingAddress);
        orders.add(order);
        System.out.println("✓ Order created: " + orderNumber);
        return order;
    }

    /**
     * Updates an order's payment status
     */
    public static void updatePaymentStatus(String orderId, String newPaymentStatus) {
        if (!ValidationService.validatePaymentStatus(newPaymentStatus)) {
            System.out.println("✗ Invalid payment status: " + newPaymentStatus);
            return;
        }

        Order order = getOrderById(orderId);
        if (order != null) {
            String oldStatus = order.getPaymentStatus();
            order.setPaymentStatus(newPaymentStatus);
            System.out.println("✓ Payment status updated: " + oldStatus + " → " + newPaymentStatus);

            // Handle status transitions
            if ("Paid".equals(newPaymentStatus)) {
                order.setOrderStatus("Delivered");
                System.out.println("✓ Order status auto-updated to Delivered");
                EmailNotifier.sendOrderPaidNotification(order);
            } else if ("Failed".equals(newPaymentStatus)) {
                cancelOrder(orderId);
                EmailNotifier.sendOrderCancelledNotification(order);
            }
        } else {
            System.out.println("✗ Order not found with ID: " + orderId);
        }
    }

    /**
     * Cancels an order and removes related items
     */
    public static void cancelOrder(String orderId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setOrderStatus("Canceled");
            order.setPaymentStatus("Failed");
            System.out.println("✓ Order cancelled: " + order.getOrderNumber());
            // In-memory cleanup handled by caller
        } else {
            System.out.println("✗ Order not found with ID: " + orderId);
        }
    }

    /**
     * Adds an order item and recalculates total
     */
    public static void addOrderItem(String orderId, OrderItem item) {
        Order order = getOrderById(orderId);
        if (order != null) {
            // Update order status to Processing if it's still New
            if ("New".equals(order.getOrderStatus())) {
                order.setOrderStatus("Processing");
                System.out.println("✓ Order status updated to Processing (first item added)");
            }
            System.out.println("✓ OrderItem added for Order " + order.getOrderNumber());
        } else {
            System.out.println("✗ Order not found with ID: " + orderId);
        }
    }

    /**
     * Calculates and updates total amount for an order based on order items
     */
    public static void calculateOrderTotal(String orderId, double itemsTotal) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setTotalAmount(itemsTotal);
            // Auto-set payment status based on total
            if (itemsTotal > 0) {
                order.setPaymentStatus("Pending");
            } else {
                order.setPaymentStatus("Paid");
            }
            System.out.println("✓ Order total calculated: $" + String.format("%.2f", itemsTotal));
        }
    }

    /**
     * Retrieves an order by ID
     */
    public static Order getOrderById(String orderId) {
        return orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves an order by order number (e.g., "ORD-0001")
     */
    public static Order getOrderByNumber(String orderNumber) {
        return orders.stream()
                .filter(o -> o.getOrderNumber().equals(orderNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all orders
     */
    public static List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    /**
     * Deletes an order and related items/shipments
     */
    public static void deleteOrder(String orderId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            orders.remove(order);
            System.out.println("✓ Order deleted: " + order.getOrderNumber());
        }
    }
}

package com.agriedge;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Double> orderItemsTotals = new HashMap<>();  // orderId -> total

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          AgriEdge Or-Mange Ltd - Order Management System        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        // Initialize sample inventory
        InventoryService.initializeSampleInventory();
        System.out.println("✓ Inventory system initialized with sample data\n");

        boolean running = true;
        while (running) {
            showMainMenu();
            System.out.print("\nSelect an option (1-7): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createNewOrder();
                    break;
                case "2":
                    addOrderItem();
                    break;
                case "3":
                    updatePaymentStatus();
                    break;
                case "4":
                    viewAllOrders();
                    break;
                case "5":
                    viewAllShipments();
                    break;
                case "6":
                    viewAllInventory();
                    break;
                case "7":
                    running = false;
                    System.out.println("\n✓ Exiting AgriEdge OMS. Goodbye!\n");
                    break;
                default:
                    System.out.println("✗ Invalid option. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                        MAIN MENU                               │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 1. Create New Order                                            │");
        System.out.println("│ 2. Add Order Item (to existing order)                          │");
        System.out.println("│ 3. Update Payment Status                                       │");
        System.out.println("│ 4. View All Orders                                             │");
        System.out.println("│ 5. View All Shipments                                          │");
        System.out.println("│ 6. View Inventory Status                                       │");
        System.out.println("│ 7. Exit                                                        │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }

    private static void createNewOrder() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CREATE NEW ORDER");
        System.out.println("=".repeat(70));

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();

        if (customerName.isEmpty()) {
            System.out.println("✗ Customer name cannot be empty");
            return;
        }

        System.out.print("Enter shipping address: ");
        String shippingAddress = scanner.nextLine().trim();

        if (shippingAddress.isEmpty()) {
            System.out.println("✗ Shipping address cannot be empty");
            return;
        }

        Order order = OrderService.createOrder(customerName, shippingAddress);
        orderItemsTotals.put(order.getId(), 0.0);
        System.out.println("✓ Order ID: " + order.getId());
        System.out.println("✓ Order Number: " + order.getOrderNumber());
        System.out.println("✓ Status: New | Payment: Pending");
    }

    private static void addOrderItem() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ADD ORDER ITEM");
        System.out.println("=".repeat(70));

        List<Order> orders = OrderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("✗ No orders found. Create an order first.");
            return;
        }

        System.out.println("\nAvailable Orders:");
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            System.out.println("  " + (i + 1) + ". " + o.getOrderNumber() + " - " + o.getCustomerName() + " (" + o.getOrderStatus() + ")");
        }

        System.out.print("\nSelect order number (1-" + orders.size() + "): ");
        int orderIndex;
        try {
            orderIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (orderIndex < 0 || orderIndex >= orders.size()) {
                System.out.println("✗ Invalid selection");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Please enter a valid number");
            return;
        }

        Order order = orders.get(orderIndex);

        System.out.print("Enter product name: ");
        String product = scanner.nextLine().trim();

        if (product.isEmpty()) {
            System.out.println("✗ Product name cannot be empty");
            return;
        }

        // Check inventory
        if (!InventoryService.checkStock(product, 1)) {
            System.out.print("Product not in system. Add it anyway? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                return;
            }
        }

        System.out.print("Enter quantity: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine().trim());
            if (quantity <= 0) {
                System.out.println("✗ Quantity must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Please enter a valid number");
            return;
        }

        System.out.print("Enter unit price: $");
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(scanner.nextLine().trim());
            if (unitPrice < 0) {
                System.out.println("✗ Price cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Please enter a valid price");
            return;
        }

        String itemId = UUID.randomUUID().toString();
        OrderItem item = new OrderItem(itemId, order.getId(), product, quantity, unitPrice);
        
        OrderService.addOrderItem(order.getId(), item);
        System.out.println("✓ Item added: " + product + " x" + quantity + " @ $" + String.format("%.2f", unitPrice) + " = $" + String.format("%.2f", item.getTotalPrice()));

        // Update order total
        double currentTotal = orderItemsTotals.getOrDefault(order.getId(), 0.0);
        double newTotal = currentTotal + item.getTotalPrice();
        orderItemsTotals.put(order.getId(), newTotal);

        OrderService.calculateOrderTotal(order.getId(), newTotal);

        // Create shipment if not exists and order is Processing
        if ("Processing".equals(order.getOrderStatus())) {
            if (ShipmentService.getShipmentByOrderId(order.getId()) == null) {
                ShipmentService.createShipment(order.getId());
                EmailNotifier.sendShipmentNotification(order, ShipmentService.getShipmentByOrderId(order.getId()));
            }
        }
    }

    private static void updatePaymentStatus() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("UPDATE PAYMENT STATUS");
        System.out.println("=".repeat(70));

        List<Order> orders = OrderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("✗ No orders found.");
            return;
        }

        System.out.println("\nAvailable Orders:");
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            System.out.println("  " + (i + 1) + ". " + o.getOrderNumber() + " - " + o.getCustomerName() + " (Status: " + o.getOrderStatus() + ", Payment: " + o.getPaymentStatus() + ")");
        }

        System.out.print("\nSelect order number (1-" + orders.size() + "): ");
        int orderIndex;
        try {
            orderIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (orderIndex < 0 || orderIndex >= orders.size()) {
                System.out.println("✗ Invalid selection");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Please enter a valid number");
            return;
        }

        Order order = orders.get(orderIndex);

        System.out.println("\nPayment Status Options:");
        System.out.println("  1. Pending");
        System.out.println("  2. Paid");
        System.out.println("  3. Failed");

        System.out.print("\nSelect new payment status (1-3): ");
        String statusChoice = scanner.nextLine().trim();

        String newStatus = null;
        switch (statusChoice) {
            case "1":
                newStatus = "Pending";
                break;
            case "2":
                newStatus = "Paid";
                break;
            case "3":
                newStatus = "Failed";
                break;
            default:
                System.out.println("✗ Invalid selection");
                return;
        }

        System.out.println("\n--- Processing Payment Update ---");
        OrderService.updatePaymentStatus(order.getId(), newStatus);

        // If payment failed, clean up
        if ("Failed".equals(newStatus)) {
            ShipmentService.deleteShipmentsByOrderId(order.getId());
            orderItemsTotals.remove(order.getId());
        }

        // If payment is Paid, update shipment to Delivered
        if ("Paid".equals(newStatus)) {
            Shipment shipment = ShipmentService.getShipmentByOrderId(order.getId());
            if (shipment != null) {
                ShipmentService.updateShipmentStatus(shipment.getId(), "Delivered");
            }
        }
    }

    private static void viewAllOrders() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ALL ORDERS");
        System.out.println("=".repeat(70));

        List<Order> orders = OrderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println(String.format("\n%-10s %-15s %-20s %-15s %-12s %-10s", "Order #", "Customer", "Status", "Payment", "Total", "Address"));
        System.out.println("-".repeat(70));
        
        for (Order o : orders) {
            System.out.println(String.format("%-10s %-15s %-20s %-15s $%-9.2f %-10s", 
                o.getOrderNumber(), 
                o.getCustomerName(), 
                o.getOrderStatus(), 
                o.getPaymentStatus(), 
                o.getTotalAmount(),
                o.getShippingAddress().substring(0, Math.min(10, o.getShippingAddress().length()))));
        }
    }

    private static void viewAllShipments() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ALL SHIPMENTS");
        System.out.println("=".repeat(70));

        List<Shipment> shipments = ShipmentService.getAllShipments();
        if (shipments.isEmpty()) {
            System.out.println("No shipments found.");
            return;
        }

        System.out.println(String.format("\n%-15s %-20s %-20s %-15s %-15s", "Order ID", "Tracking #", "Carrier", "Status", "Order #"));
        System.out.println("-".repeat(70));
        
        for (Shipment s : shipments) {
            Order order = OrderService.getOrderById(s.getOrderId());
            String orderNumber = order != null ? order.getOrderNumber() : "N/A";
            System.out.println(String.format("%-15s %-20s %-20s %-15s %-15s", 
                s.getOrderId().substring(0, 8) + "...", 
                s.getTrackingNumber().substring(0, Math.min(20, s.getTrackingNumber().length())), 
                s.getCarrier(), 
                s.getStatus(),
                orderNumber));
        }
    }

    private static void viewAllInventory() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INVENTORY STATUS");
        System.out.println("=".repeat(70));

        List<Inventory> inventoryList = InventoryService.getAllInventory();
        if (inventoryList.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }

        System.out.println(String.format("\n%-20s %-10s %-10s %-12s %-20s %-10s", "Product", "Stock", "Reorder", "Status", "Location", "ID"));
        System.out.println("-".repeat(70));
        
        for (Inventory inv : inventoryList) {
            String statusIcon = "Low".equals(inv.getStockStatus()) ? "⚠ Low" : "✓ OK";
            System.out.println(String.format("%-20s %-10d %-10d %-12s %-20s %-10s", 
                inv.getProduct(), 
                inv.getStockQuantity(), 
                inv.getReorderLevel(), 
                statusIcon,
                inv.getWarehouseLocation(),
                inv.getId()));
        }
    }
}

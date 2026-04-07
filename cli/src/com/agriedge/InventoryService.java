package com.agriedge;

import java.util.*;

public class InventoryService {
    private static final List<Inventory> inventory = new ArrayList<>();

    /**
     * Initializes default inventory items for demo
     */
    public static void initializeSampleInventory() {
        addInventoryItem("INV-001", "Corn Seeds", 100, 20, "Warehouse A");
        addInventoryItem("INV-002", "Fertilizer", 50, 15, "Warehouse B");
        addInventoryItem("INV-003", "Pesticide", 30, 10, "Warehouse C");
        addInventoryItem("INV-004", "Farm Tools", 25, 5, "Warehouse A");
    }

    /**
     * Adds an inventory item
     */
    public static Inventory addInventoryItem(String id, String product, int stockQuantity, int reorderLevel, String warehouseLocation) {
        if (!ValidationService.validateInventoryLevel(stockQuantity, reorderLevel)) {
            System.out.println("⚠ Warning: Stock quantity (" + stockQuantity + ") is at or below reorder level (" + reorderLevel + ")");
        }
        Inventory inv = new Inventory(id, product, stockQuantity, reorderLevel, warehouseLocation);
        inventory.add(inv);
        return inv;
    }

    /**
     * Checks if stock is available for a product
     */
    public static boolean checkStock(String product, int quantity) {
        Inventory inv = getInventoryByProduct(product);
        if (inv == null) {
            System.out.println("✗ Product not found in inventory: " + product);
            return false;
        }
        return inv.getStockQuantity() >= quantity;
    }

    /**
     * Updates stock after order (decreases stock)
     */
    public static void updateStock(String product, int quantity, boolean isDeduction) {
        Inventory inv = getInventoryByProduct(product);
        if (inv != null) {
            int newStock = isDeduction ? inv.getStockQuantity() - quantity : inv.getStockQuantity() + quantity;
            inv.setStockQuantity(newStock);
            System.out.println("✓ Inventory updated: " + product + " → " + newStock + " units");
            
            if (newStock <= inv.getReorderLevel()) {
                System.out.println("⚠ WARNING: Stock for " + product + " is now LOW. Reorder level: " + inv.getReorderLevel());
            }
        }
    }

    /**
     * Gets stock status for a product
     */
    public static String getStockStatus(String product) {
        Inventory inv = getInventoryByProduct(product);
        return inv != null ? inv.getStockStatus() : "Unknown";
    }

    /**
     * Retrieves inventory by product name
     */
    public static Inventory getInventoryByProduct(String product) {
        return inventory.stream()
                .filter(inv -> inv.getProduct().equalsIgnoreCase(product))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all inventory items
     */
    public static List<Inventory> getAllInventory() {
        return new ArrayList<>(inventory);
    }

    /**
     * Gets inventory by ID
     */
    public static Inventory getInventoryById(String id) {
        return inventory.stream()
                .filter(inv -> inv.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

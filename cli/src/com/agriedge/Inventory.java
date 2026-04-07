package com.agriedge;

public class Inventory {
    private String id;
    private String product;
    private int stockQuantity;
    private int reorderLevel;
    private String warehouseLocation;

    public Inventory(String id, String product, int stockQuantity, int reorderLevel, String warehouseLocation) {
        this.id = id;
        this.product = product;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.warehouseLocation = warehouseLocation;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public String getWarehouseLocation() { return warehouseLocation; }
    public void setWarehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; }

    public String getStockStatus() {
        return stockQuantity <= reorderLevel ? "Low" : "Sufficient";
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id='" + id + '\'' +
                ", product='" + product + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", reorderLevel=" + reorderLevel +
                ", warehouseLocation='" + warehouseLocation + '\'' +
                ", status='" + getStockStatus() + '\'' +
                '}';
    }
}

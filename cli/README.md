# AgriEdge OMS - CLI Application

A Java-based Command-Line Interface (CLI) application that simulates the Salesforce Order Management System (OMS) for AgriEdge Or-Mange Ltd. This application demonstrates order processing, inventory management, shipment tracking, and payment workflows with in-memory data storage.

---

## Project Structure

```
cli/
├── src/
│   └── com/
│       └── agriedge/
│           ├── Main.java                    # Menu-driven CLI entry point
│           ├── Order.java                   # Order model
│           ├── OrderItem.java               # Order item model
│           ├── Inventory.java               # Inventory model
│           ├── Shipment.java                # Shipment model
│           ├── OrderService.java            # Order business logic
│           ├── InventoryService.java        # Inventory business logic
│           ├── ShipmentService.java         # Shipment business logic
│           ├── ValidationService.java       # Validation rules
│           └── EmailNotifier.java           # Email notification simulator
├── compile.bat                # Windows compile script
├── run.bat                     # Windows run script
├── compile.sh                  # Unix/Linux/Mac compile script
├── run.sh                      # Unix/Linux/Mac run script
└── README.md                   # This file
```

---

## Requirements

- **Java Development Kit (JDK)**: Version 8 or higher
- **Operating System**: Windows, Linux, macOS, or any OS with Java support
- **Command Line**: Terminal, Command Prompt, or PowerShell

**Verify Java installation:**
```bash
java -version
javac -version
```

---

## Getting Started

### On Windows

1. Open Command Prompt or PowerShell
2. Navigate to the `cli` directory:
   ```cmd
   cd path\to\AgriEdge-OMS\cli
   ```
3. Compile the project:
   ```cmd
   compile.bat
   ```
4. Run the application:
   ```cmd
   run.bat
   ```

### On Linux / macOS

1. Open Terminal
2. Navigate to the `cli` directory:
   ```bash
   cd path/to/AgriEdge-OMS/cli
   ```
3. Make scripts executable (first time only):
   ```bash
   chmod +x compile.sh run.sh
   ```
4. Compile the project:
   ```bash
   ./compile.sh
   ```
5. Run the application:
   ```bash
   ./run.sh
   ```

---

## Manual Compilation and Execution

If you prefer not to use the provided scripts:

### Compile
```bash
cd src
javac -d ../bin com/agriedge/*.java
cd ..
```

### Run
```bash
java -cp bin com.agriedge.Main
```

---

## Features

### 1. Create New Orders
- Auto-generates order numbers in format: `ORD-0001`, `ORD-0002`, etc.
- Initializes with status `New` and payment status `Pending`
- Requires customer name and shipping address

### 2. Add Order Items
- Add line items to existing orders
- Auto-calculates item total price (Quantity × Unit Price)
- Updates order status to `Processing` when first item is added
- Auto-creates shipment records
- Triggers email notification for shipment

### 3. Update Payment Status
- Change payment status: `Pending` → `Paid` → `Failed`
- **Paid**: Order status updates to `Delivered`, shipment updates to `Delivered`, confirmation email sent
- **Failed**: Order cancelled, related shipments deleted, cancellation email sent

### 4. Inventory Management
- Pre-populated with sample products: Corn Seeds, Fertilizer, Pesticide, Farm Tools
- Track stock quantity and reorder levels
- Visual alerts when stock falls below reorder level (⚠ Low)
- Stock status: "Low" or "Sufficient"

### 5. Shipment Tracking
- Auto-creates shipments with test tracking numbers (`TEST_{orderId}`)
- Supports carriers: FedEx, UPS, DHL, Local Courier
- Track shipment status: `Pending` → `In Transit` → `Delivered`

### 6. Email Notifications
- Simulated email sending with detailed console output
- Sent for: Payment Confirmed, Order Cancelled, Shipment Updates
- Contains realistic order and shipment details

### 7. Validation Rules
- Order Status Change Restriction: Prevents marking shipment as Delivered without tracking number
- Inventory Record Alert: Warns when stock falls to or below reorder level
- Payment Status Validation: Ensures valid payment states
- Order Status Validation: Ensures valid order states

---

## Demo Workflow

Follow these steps to test the complete system:

### Step 1: Create a New Order
```
Menu Option: 1
Customer Name: Farmer John
Shipping Address: 123 Agricultural Lane, Rural County
```
✓ Order created: `ORD-0001`

### Step 2: Add Order Items
```
Menu Option: 2
Select Order: 1 (ORD-0001 - Farmer John)
Product: Corn Seeds
Quantity: 100
Unit Price: 25.50
```
✓ Order Item added, Order Status → `Processing`
✓ Shipment created automatically
✓ Email notification sent

### Step 3: Update Payment to Paid
```
Menu Option: 3
Select Order: 1 (ORD-0001 - Farmer John)
New Payment Status: 2 (Paid)
```
✓ Payment status updated: Pending → Paid
✓ Order status updated: Processing → Delivered
✓ Shipment status updated: Pending → Delivered
✓ Order Paid Notification email sent

### Step 4: View All Orders
```
Menu Option: 4
```
Displays all orders with order number, customer, status, payment status, total, and shipping address.

### Step 5: View All Shipments
```
Menu Option: 5
```
Displays all shipments with tracking numbers, carrier, and status.

### Step 6: View Inventory Status
```
Menu Option: 6
```
Displays all inventory items with stock quantity, reorder level, warehouse location, and status.

### Alternative: Test Payment Failure
```
Menu Option: 3
Select Order: 1
New Payment Status: 3 (Failed)
```
✓ Order status updated to Canceled
✓ Related shipments deleted
✓ Order Cancelled Notification email sent

---

## In-Memory Data Storage

All data is stored in memory using:
- **ArrayList**: OrderService, InventoryService, ShipmentService uses ArrayLists to store records
- **HashMap**: Main class uses HashMap for tracking order items and totals
- **No Database**: Application does not require any external database
- **No Persistence**: Data is lost when application terminates

---

## Class Descriptions

### Models
- **Order.java**: Represents an order with customer info, status, and payment details
- **OrderItem.java**: Represents a line item in an order with product, quantity, and price
- **Inventory.java**: Represents stock for a product with quantity and reorder level
- **Shipment.java**: Represents a shipment with tracking number and carrier

### Services
- **OrderService.java**: Creates orders, updates payment status, calculates totals
- **InventoryService.java**: Manages stock levels, checks availability, tracks status
- **ShipmentService.java**: Creates shipments, updates status, tracks by order

### Utilities
- **ValidationService.java**: Validates business rules (tracking numbers, stock levels)
- **EmailNotifier.java**: Simulates email notifications with formatted console output

### Main
- **Main.java**: Menu-driven CLI with user interaction and workflow orchestration

---

## Troubleshooting

### Compilation Error: "javac is not recognized"
**Solution**: Java Development Kit (JDK) is not installed or not in PATH.
- Download and install JDK from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
- Add JDK bin directory to system PATH

### Compilation Error: "package com.agriedge does not exist"
**Solution**: Running javac from wrong directory.
- Ensure you're in the `cli` directory when compiling
- Use the provided compile scripts instead

### Runtime Error: "Could not find or load main class"
**Solution**: Class files were not compiled properly.
- Delete the `bin` directory
- Re-run the compile script
- Ensure `bin/com/agriedge/Main.class` exists

### Script Permission Denied (Linux/macOS)
**Solution**: Script files need execute permission.
```bash
chmod +x compile.sh run.sh
```

---

## Features Implemented (Aligned with Salesforce OMS)

✓ Auto-numbered Order Numbers (ORD-0001, etc.)
✓ Order Status Management (New → Processing → Shipped → Delivered → Canceled)
✓ Payment Status Tracking (Pending, Paid, Failed)
✓ Order Item Aggregation (Auto-calculate totals)
✓ Inventory Stock Status (Low/Sufficient)
✓ Shipment Creation & Tracking
✓ Payment Status Change Logic
✓ Cascade Deletion on Failed Payments
✓ Email Notifications
✓ Validation Rules
✓ In-Memory Storage
✓ CLI Menu-Driven Interface

---

## Example Session Output

```
╔════════════════════════════════════════════════════════════════╗
║          AgriEdge Or-Mange Ltd - Order Management System        ║
╚════════════════════════════════════════════════════════════════╝

✓ Inventory system initialized with sample data

┌────────────────────────────────────────────────────────────────┐
│                        MAIN MENU                               │
├────────────────────────────────────────────────────────────────┤
│ 1. Create New Order                                            │
│ 2. Add Order Item (to existing order)                          │
│ 3. Update Payment Status                                       │
│ 4. View All Orders                                             │
│ 5. View All Shipments                                          │
│ 6. View Inventory Status                                       │
│ 7. Exit                                                        │
└────────────────────────────────────────────────────────────────┘

Select an option (1-7): 1

======================================================================
CREATE NEW ORDER
======================================================================
Enter customer name: John Farmer
Enter shipping address: 123 Agri Lane, Farm City
✓ Order created: ORD-0001
✓ Order ID: a1b2c3d4-e5f6-47d8-9k0l-m1n2o3p4q5r6
✓ Order Number: ORD-0001
✓ Status: New | Payment: Pending
```

---

## Future Enhancements

- [ ] File-based persistence (CSV/JSON export)
- [ ] Database integration (MySQL, PostgreSQL)
- [ ] REST API layer
- [ ] Advanced reporting and analytics
- [ ] Multi-user authentication
- [ ] Order filtering and searching
- [ ] Batch operations
- [ ] GUI interface using Swing or JavaFX

---

## Contact & Support

For issues or questions about this CLI application, please refer to the main [AgriEdge OMS README](../README.md).

---

## License

This project is part of the AgriEdge-OMS initiative.

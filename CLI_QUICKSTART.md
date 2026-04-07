# AgriEdge OMS - CLI Quick Start Guide

This directory contains the CLI (Command-Line Interface) implementation of the AgriEdge Order Management System.

## 🚀 Quick Start

### Windows
```batch
cd cli
compile.bat
run.bat
```

### Linux / macOS
```bash
cd cli
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

## 📋 Features

The CLI application simulates the complete Salesforce OMS workflow:

1. **Create Orders** - Auto-generates order numbers (ORD-0001, etc.)
2. **Add Order Items** - Calculate totals, update order status
3. **Update Payments** - Trigger status changes and notifications
4. **Manage Inventory** - Track stock and reorder levels
5. **Track Shipments** - Monitor delivery status
6. **Email Notifications** - Simulated email alerts
7. **Validation Rules** - Business rule enforcement

## 🎯 Demo Workflow

```
1. Create an order (Status: New)
   ↓
2. Add order items (Status: Processing, Shipment created)
   ↓
3. Update payment to Paid (Status: Delivered, Email sent)
   ↓
4. Or update payment to Failed (Order Canceled, cleanup)
```

## 📁 Project Structure

```
cli/
├── src/com/agriedge/          # Java source files
│   ├── Main.java              # CLI entry point
│   ├── Order.java             # Order model
│   ├── OrderService.java      # Business logic
│   ├── ValidationService.java # Validation rules
│   └── ... (7 more files)
├── bin/                       # Compiled classes (generated)
├── compile.bat/sh             # Build scripts
├── run.bat/sh                 # Run scripts
└── README.md                  # Full documentation
```

## 📝 Requirements

- Java 8 or higher
- 50+ MB free disk space
- Command line / Terminal access

## 🔍 For More Information

See [cli/README.md](cli/README.md) for complete documentation.

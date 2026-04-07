# AgriEdge Or-Mange Ltd — Salesforce Order Management System

## Project Overview

A Salesforce-driven Order Management System (OMS) built for AgriEdge Or-Mange Ltd, an agriculture and food production company. The system automates order processing, tracks inventory in real-time, integrates with customer service channels, and enforces data security — replacing error-prone manual workflows.

---

## Tech Stack

- Platform: Salesforce (Lightning Experience)
- Languages: Apex (Classes, Triggers, Test Classes)
- Automation: Process Builder
- UI: Lightning App with custom tabs and navigation

---

## Phase 1 — Data Modelling

Four custom objects form the data backbone.

**AgriEdge_Order__c** stores order records with auto-numbered Order Numbers (format: ORD-0000), a lookup to Account for the customer, picklist fields for Order Status (New, Processing, Shipped, Delivered, Canceled) and Payment Status (Pending, Paid, Failed), date/time, currency, shipping address text area, and a formula field `Discounted_Total__c = Total_Amount__c - (Total_Amount__c * 0.1)`.

**AgriEdge_OrderItem__c** stores line items linked to an order and a product, with Quantity, Unit Price, and a formula `Total_Price__c = Quantity__c * Unit_Price__c`.

**AgriEdge_Inventory__c** tracks product stock with Stock Quantity, Reorder Level, Warehouse Location, and a formula `Stock_Status__c = IF(Stock_Quantity__c <= Reorder_Level__c, "Low", "Sufficient")`.

**AgriEdge_Shipment__c** tracks shipments linked to orders with a Tracking Number, Carrier picklist (FedEx, UPS, DHL, Local Courier), and Status picklist (Pending, In Transit, Delivered).

Custom tabs are created for all four objects and added to a Lightning App named **AgriEdge** with navigation items: Orders, OrderItems, Inventory, Shipments, Reports, Dashboard.

---

## Phase 2 — Validation Rules

**Order Status Change Restriction** (on AgriEdge_Shipment__c): Prevents saving a shipment if the related order is marked Delivered but the tracking number is blank.
Formula: `AND(ISPICKVAL(AgriEdge_Order__r.Order_Status__c, 'Delivered'), ISBLANK(Tracking_Number__c))`
Error: *"Tracking Number is required before marking order as Delivered."*

**Inventory Record Alert** (on AgriEdge_Inventory__c): Blocks saving an inventory record when stock falls at or below reorder level.
Formula: `Stock_Quantity__c <= Reorder_Level__c`
Error: *"Stock is below reorder level. Please restock."* (displayed at top of page)

---

## Phase 3 — Roles, Profiles & Users

**Roles hierarchy:**
- CEO (top)
  - Sales Representative → reports to CEO
  - Warehouse Manager → reports to CEO
  - Finance Team → reports to Sales Representative

**Profiles:**
- Platform 1 (based on Standard Platform User): Read/Create on AgriEdge Orders and OrderItems
- Platform 2: Read/Create/Edit on AgriEdge Inventory, Account, AgriEdge Shipment; Read-Only on AgriEdge Order and OrderItems
- Platform 3: Read/Create/Edit on AgriEdge Order and OrderItems

**Users:**
- John Sandbox (Sales Rep, Platform 1)
- Mike Quality Inspector (Warehouse Manager, Platform 2)
- Albert Plant Manager (Finance Team, Platform 3)

**Field Level Security:** Payment Status, Discounted Total, and Total Amount fields on AgriEdge Order are hidden from Platform 1 and Platform 2 profiles.

---

## Phase 4 — Apex Classes & Triggers

### OrderTaskCreator
Invocable method called from Process Builder on new order creation. Queries all users with Profile Name 'Platform 1' and creates a high-priority Task linked to each new order, instructing them to create an OrderItem record.

### OrderStatusUpdater
Static method that accepts a set of Order IDs and updates any order still in "New" status to "Processing". Called from `OrderItemTrigger`.

### OrderTotalUpdater
Static method that aggregates Total_Price__c from all OrderItems grouped by Order ID, updates Total_Amount__c on the parent order, and sets Payment_Status__c to "Pending" if total > 0, else "Paid". Called from `OrderItemTrigger`.

### OrderItemTrigger (after insert, after update on AgriEdge_OrderItem__c)
Collects Order IDs from inserted/updated OrderItems, then calls both `OrderStatusUpdater.updateOrderStatus()` and `OrderTotalUpdater.updateOrderTotal()`.

### OrderEmailSender
Static method that fetches orders by ID, collects related Account contacts, builds an HTML email body with order details (name, status, total, payment status, shipping address, discounted total), and sends emails to all contacts when Payment Status changes to "Paid".

### OrderPaymentStatusTrigger (after update on AgriEdge_Order__c)
Detects when Payment_Status__c changes to "Paid" and calls `OrderEmailSender.sendOrderEmail()`.

### AgriEdgeOrderTriggerHelper
Boolean flag class (`isTriggerExecuted`) used to prevent recursive trigger execution.

### AgriEdgeOrderShipmentHelper
Helper class called from `AgriEdgeOrderTrigger`. Handles:
- Payment Paid + not yet Delivered → sets Order Status to Delivered
- Payment Pending → sets Order Status to Processing
- Payment Failed → sets Order Status to Canceled, deletes related OrderItems and Shipments
- Order Processing with no existing shipment → creates a new Shipment record (Status: Pending, Tracking: TEST_{orderId})
- Order Shipped/Delivered → updates existing Shipment Status to In Transit or Delivered

### AgriEdgeOrderTrigger (after insert, after update on AgriEdge_Order__c)
Uses the helper flag to prevent recursion. On insert or when Payment Status / Order Status changes: updates Order Status based on Payment Status (Pending → Processing, Failed → Canceled), deletes related OrderItems and Shipments for failed orders, then calls `AgriEdgeOrderShipmentHelper.processOrderStatusChange()`.

### AgriEdgeOrderTests (Test Class — target: 75–100% coverage)
Covers all classes with isolated test methods:
- `testOrderTaskCreator`: Creates an account, Platform 1 user, and order; verifies tasks are created
- `testOrderStatusUpdater`: Inserts orders in various statuses; verifies only "New" orders move to "Processing"
- `testOrderTotalUpdater`: Inserts OrderItems; verifies total aggregation and payment status logic
- `testSendOrderEmail`: Creates account, contacts, and orders; verifies email invocation count
- `testAgriEdgeOrderShipmentHelper`: Verifies shipment creation for a Processing order
- `testAgriEdgeOrderTrigger`: Updates Payment Status to Paid; verifies Order Status becomes Delivered
- `testAgriEdgeOrderTriggerHelper`: Verifies the recursion flag toggles correctly

---

## Phase 5 — Process Builder

**Process: Order Task Creator**
- Trigger object: AgriEdge_Order__c
- Starts when: Record is created only
- Criteria: Order_Status__c = "New"
- Action: Calls Apex class `OrderTaskCreator` (InvocableMethod)
- Activated after configuration

---

## Demo Flow (for CLI simulation / video walkthrough)

1. Create an AgriEdge Order (Status: New, Payment: Pending) → Process Builder fires → Task created for Platform 1 user
2. Add OrderItems → Trigger fires → Total Amount updates, Order Status moves to Processing, Shipment record created
3. Update Payment Status to Paid → Order moves to Delivered, Shipment updates to Delivered, email sent to customer contacts
4. Update Payment Status to Failed → Order canceled, OrderItems and Shipment deleted
5. Test Validation Rules: try saving Shipment without tracking number on a Delivered order; try saving Inventory below reorder level

const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'public')));

// In-Memory Storage
let orders = [];
let orderItems = [];
let inventory = [];
let shipments = [];
let orderCounter = 1;

// Initialize Sample Inventory
function initializeInventory() {
  inventory = [
    { id: 'INV-001', product: 'Corn Seeds', stockQuantity: 100, reorderLevel: 20, warehouseLocation: 'Warehouse A' },
    { id: 'INV-002', product: 'Fertilizer', stockQuantity: 50, reorderLevel: 15, warehouseLocation: 'Warehouse B' },
    { id: 'INV-003', product: 'Pesticide', stockQuantity: 30, reorderLevel: 10, warehouseLocation: 'Warehouse C' },
    { id: 'INV-004', product: 'Farm Tools', stockQuantity: 25, reorderLevel: 5, warehouseLocation: 'Warehouse A' }
  ];
}

// Utility Functions
function generateOrderNumber() {
  return `ORD-${String(orderCounter++).padStart(4, '0')}`;
}

function getStockStatus(stockQuantity, reorderLevel) {
  return stockQuantity <= reorderLevel ? 'Low' : 'Sufficient';
}

function generateTrackingNumber(orderId) {
  return `TEST_${orderId}`;
}

// Initialize inventory on startup
initializeInventory();

// ============== API ROUTES ==============

// GET / - Serve index.html
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// ============== ORDERS API ==============

// POST /api/orders - Create new order
app.post('/api/orders', (req, res) => {
  try {
    const { customerName, shippingAddress } = req.body;

    if (!customerName || !shippingAddress) {
      return res.status(400).json({ error: 'Customer name and shipping address are required' });
    }

    const order = {
      id: require('crypto').randomUUID(),
      orderNumber: generateOrderNumber(),
      customerName,
      orderStatus: 'New',
      paymentStatus: 'Pending',
      totalAmount: 0,
      shippingAddress,
      createdAt: new Date().toISOString()
    };

    orders.push(order);
    console.log(`✓ Order created: ${order.orderNumber}`);
    res.status(201).json(order);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// GET /api/orders - Get all orders
app.get('/api/orders', (req, res) => {
  try {
    const ordersWithItems = orders.map(order => {
      const items = orderItems.filter(item => item.orderId === order.id);
      return { ...order, items };
    });
    res.json(ordersWithItems);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// GET /api/orders/:id - Get single order
app.get('/api/orders/:id', (req, res) => {
  try {
    const order = orders.find(o => o.id === req.params.id);
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }
    const items = orderItems.filter(item => item.orderId === order.id);
    res.json({ ...order, items });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// PATCH /api/orders/:id/payment - Update payment status
app.patch('/api/orders/:id/payment', (req, res) => {
  try {
    const { paymentStatus } = req.body;
    const validStatuses = ['Pending', 'Paid', 'Failed'];

    if (!validStatuses.includes(paymentStatus)) {
      return res.status(400).json({ error: 'Invalid payment status' });
    }

    const order = orders.find(o => o.id === req.params.id);
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }

    const oldStatus = order.paymentStatus;
    order.paymentStatus = paymentStatus;

    // Update order status based on payment status
    if (paymentStatus === 'Paid') {
      order.orderStatus = 'Delivered';
      
      // Update shipment if exists
      const shipment = shipments.find(s => s.orderId === order.id);
      if (shipment) {
        shipment.status = 'Delivered';
      }
      
      // Simulate email notification
      console.log(`\n${'='.repeat(70)}`);
      console.log('📧 EMAIL NOTIFICATION SENT');
      console.log('='.repeat(70));
      console.log(`To: ${order.customerName}@agriedge.com`);
      console.log(`Subject: Order ${order.orderNumber} Payment Confirmed`);
      console.log('-'.repeat(70));
      console.log(`Dear ${order.customerName},`);
      console.log(`\nYour order has been received and payment confirmed!`);
      console.log(`\nOrder Details:`);
      console.log(`  Order Number: ${order.orderNumber}`);
      console.log(`  Status: ${order.orderStatus}`);
      console.log(`  Total Amount: $${order.totalAmount.toFixed(2)}`);
      console.log(`  Payment Status: ${order.paymentStatus}`);
      console.log(`  Shipping Address: ${order.shippingAddress}`);
      console.log(`  Discounted Total: $${(order.totalAmount * 0.9).toFixed(2)}`);
      console.log(`\nYour order will be shipped shortly. Thank you for your business!`);
      console.log(`\nBest regards,`);
      console.log(`AgriEdge Or-Mange Ltd`);
      console.log(`${'='.repeat(70)}\n`);
    } else if (paymentStatus === 'Failed') {
      order.orderStatus = 'Canceled';
      
      // Delete related items and shipments
      orderItems = orderItems.filter(item => item.orderId !== order.id);
      shipments = shipments.filter(s => s.orderId !== order.id);
      
      // Simulate cancellation email
      console.log(`\n${'='.repeat(70)}`);
      console.log('📧 EMAIL NOTIFICATION SENT');
      console.log('='.repeat(70));
      console.log(`To: ${order.customerName}@agriedge.com`);
      console.log(`Subject: Order ${order.orderNumber} Cancelled - Payment Failed`);
      console.log('-'.repeat(70));
      console.log(`Dear ${order.customerName},`);
      console.log(`\nUnfortunately, your order has been cancelled due to payment failure.`);
      console.log(`\nOrder Details:`);
      console.log(`  Order Number: ${order.orderNumber}`);
      console.log(`  Status: ${order.orderStatus}`);
      console.log(`  Payment Status: ${order.paymentStatus}`);
      console.log(`\nPlease update your payment method and try again.`);
      console.log(`\nBest regards,`);
      console.log(`AgriEdge Or-Mange Ltd`);
      console.log(`${'='.repeat(70)}\n`);
    }

    console.log(`✓ Payment status updated: ${oldStatus} → ${paymentStatus}`);
    res.json(order);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// ============== ORDER ITEMS API ==============

// POST /api/orders/:id/items - Add order item
app.post('/api/orders/:id/items', (req, res) => {
  try {
    const { product, quantity, unitPrice } = req.body;

    const order = orders.find(o => o.id === req.params.id);
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }

    if (!product || quantity <= 0 || unitPrice < 0) {
      return res.status(400).json({ error: 'Invalid item data' });
    }

    const item = {
      id: require('crypto').randomUUID(),
      orderId: order.id,
      product,
      quantity,
      unitPrice,
      totalPrice: quantity * unitPrice
    };

    orderItems.push(item);

    // Update order status to Processing if still New
    if (order.orderStatus === 'New') {
      order.orderStatus = 'Processing';
    }

    // Calculate new total
    const itemsForOrder = orderItems.filter(i => i.orderId === order.id);
    const newTotal = itemsForOrder.reduce((sum, i) => sum + i.totalPrice, 0);
    order.totalAmount = newTotal;

    // Auto-set payment status
    if (newTotal > 0) {
      order.paymentStatus = 'Pending';
    } else {
      order.paymentStatus = 'Paid';
    }

    // Create shipment if doesn't exist
    if (!shipments.find(s => s.orderId === order.id)) {
      const shipment = {
        id: require('crypto').randomUUID(),
        orderId: order.id,
        trackingNumber: generateTrackingNumber(order.id),
        carrier: 'Local Courier',
        status: 'Pending'
      };
      shipments.push(shipment);
      console.log(`✓ Shipment created: Tracking ${shipment.trackingNumber}`);
    }

    console.log(`✓ Item added: ${product} x${quantity} @ $${unitPrice.toFixed(2)}`);
    res.status(201).json({ item, order });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// ============== INVENTORY API ==============

// GET /api/inventory - Get all inventory
app.get('/api/inventory', (req, res) => {
  try {
    const inventoryWithStatus = inventory.map(inv => ({
      ...inv,
      status: getStockStatus(inv.stockQuantity, inv.reorderLevel)
    }));
    res.json(inventoryWithStatus);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// PATCH /api/inventory/:id - Update stock
app.patch('/api/inventory/:id', (req, res) => {
  try {
    const { stockQuantity } = req.body;
    const inv = inventory.find(i => i.id === req.params.id);

    if (!inv) {
      return res.status(404).json({ error: 'Inventory item not found' });
    }

    if (stockQuantity < 0) {
      return res.status(400).json({ error: 'Stock quantity cannot be negative' });
    }

    const oldStock = inv.stockQuantity;
    inv.stockQuantity = stockQuantity;

    console.log(`✓ Inventory updated: ${inv.product} → ${stockQuantity} units`);

    if (stockQuantity <= inv.reorderLevel) {
      console.log(`⚠ WARNING: Stock for ${inv.product} is now LOW. Reorder level: ${inv.reorderLevel}`);
    }

    res.json({
      ...inv,
      status: getStockStatus(inv.stockQuantity, inv.reorderLevel)
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// ============== SHIPMENTS API ==============

// GET /api/shipments - Get all shipments
app.get('/api/shipments', (req, res) => {
  try {
    const shipmentsWithOrder = shipments.map(ship => {
      const order = orders.find(o => o.id === ship.orderId);
      return {
        ...ship,
        orderNumber: order ? order.orderNumber : 'N/A',
        customerName: order ? order.customerName : 'N/A'
      };
    });
    res.json(shipmentsWithOrder);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// ============== ERROR HANDLING ==============

app.use((req, res) => {
  res.status(404).json({ error: 'Route not found' });
});

// Start Server
app.listen(PORT, () => {
  console.log('\n╔════════════════════════════════════════════════════════════════╗');
  console.log('║          AgriEdge Or-Mange Ltd - Order Management System        ║');
  console.log('║                    Web Application Started                      ║');
  console.log('╚════════════════════════════════════════════════════════════════╝\n');
  console.log(`✓ Server running on http://localhost:${PORT}`);
  console.log(`✓ Inventory initialized with sample data\n`);
});

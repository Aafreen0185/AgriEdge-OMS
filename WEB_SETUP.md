# AgriEdge OMS - Web Application - Complete Setup Guide

**Status:** ✅ Fully implemented and ready to run

---

## 📦 What's Been Created

A complete full-stack web application with the following components:

### Backend (Node.js + Express)
- **server.js** - Express server with 7 REST API endpoints
- **In-memory storage** - Arrays for orders, items, inventory, shipments
- **Business logic** - Auto order numbers, total calculation, payment workflows
- **Email simulation** - Console output for notifications

### Frontend (Vanilla HTML/CSS/JavaScript)
- **index.html** - Tab-based UI with 4 main sections
- **style.css** - Responsive design with modern styling
- **app.js** - Frontend logic with Fetch API calls

### Configuration
- **package.json** - Express and body-parser dependencies
- **start.bat** - Windows startup script
- **.gitignore** - Node.js project exclusions

---

## 🚀 How to Run

### Quick Start (Windows)
```cmd
cd web
npm install
npm start
```
Then open: **http://localhost:3000**

### Quick Start (Mac/Linux)
```bash
cd web
npm install
npm start
```
Or:
```bash
chmod +x start.sh
./start.sh
```

### Verify Installation
1. Navigate to `web` folder
2. Look for `node_modules/` folder (created by npm install)
3. Run `npm start` or `node server.js`
4. See the banner:
   ```
   ╔════════════════════════════════════════════════════════════════╗
   ║          AgriEdge Or-Mange Ltd - Order Management System        ║
   ║                    Web Application Started                      ║
   ╚════════════════════════════════════════════════════════════════╝
   
   ✓ Server running on http://localhost:3000
   ```

---

## 🎯 Features Implemented

### ✅ Orders Management
- Create orders with auto-generated numbers (ORD-0001, ORD-0002, etc.)
- View all orders in a table
- Update payment status (Pending → Paid → Failed)
- Order status auto-transitions based on payment
- Total amount calculation

### ✅ Order Items
- Add items to orders (product, quantity, unit price)
- Auto-calculate item totals (Qty × Price)
- Auto-update order total from all items
- Auto-create shipment when first item added
- Order status changes to "Processing" on first item

### ✅ Inventory Management
- Pre-loaded sample inventory (4 products)
- Track stock quantities
- Reorder level monitoring
- Stock status: "Low" or "Sufficient"
- Update stock quantities
- Visual alerts for low stock

### ✅ Shipment Tracking
- Auto-create shipments when items added
- Track shipment status (Pending → In Transit → Delivered)
- View all shipments with tracking numbers
- Test tracking format: TEST_{orderId}

### ✅ Payment Workflows
- Pending → Paid: Order becomes Delivered, shipment updates, email sent
- Pending → Failed: Order canceled, items deleted, shipment deleted, email sent
- Email notifications logged to console

### ✅ Validation Rules
- Prevent invalid payment statuses
- Validate order status transitions
- Check for valid shipment statuses
- Inventory alerts for low stock

### ✅ User Interface
- Tab-based navigation (Orders, Items, Inventory, Shipments)
- Responsive design (mobile, tablet, desktop)
- Modal dialogs for forms
- Toast notifications for feedback
- Color-coded status badges
- Data tables with actions

---

## 📊 Project Structure

```
AgriEdge-OMS/
├── web/                          # ← Main web app directory
│   ├── server.js                 # Express backend
│   ├── package.json              # Node dependencies
│   ├── start.bat                 # Windows startup
│   ├── start.sh                  # Unix/Linux startup
│   ├── .gitignore               # Git exclusions
│   ├── README.md                # Full documentation
│   ├── node_modules/            # Installed packages (after npm install)
│   └── public/                  # Frontend static files
│       ├── index.html           # Main UI
│       ├── style.css            # Styling
│       └── app.js               # Frontend logic
├── WEB_QUICKSTART.md            # Quick start guide
├── CLI_QUICKSTART.md            # CLI app guide
├── cli/                         # CLI version (earlier)
├── README.md                    # Project overview
└── docs/                        # Documentation
```

---

## 🔌 REST API Endpoints

### Orders
```
POST   /api/orders
GET    /api/orders
GET    /api/orders/:id
PATCH  /api/orders/:id/payment
```

### Order Items
```
POST   /api/orders/:id/items
```

### Inventory
```
GET    /api/inventory
PATCH  /api/inventory/:id
```

### Shipments
```
GET    /api/shipments
```

---

## 💡 Demo Workflow

### Step 1: Create Order
1. Click **Orders** tab
2. Click **+ New Order**
3. Enter: Name = "John Farmer", Address = "123 Farm Lane"
4. Click **Create Order**
   - Result: ORD-0001 created (Status: New, Payment: Pending)

### Step 2: Add Items
1. Click **Items** tab
2. Select order from dropdown
3. Enter: Product = "Corn Seeds", Qty = "100", Price = "25.50"
4. Click **Add Item to Order**
   - Result: Item added, Order → Processing, Shipment created, Total = $2,550

### Step 3: Update Payment to Paid
1. Click **Orders** tab
2. Click **Update Payment** button on the order
3. Select **Paid**
4. Click **Update**
   - Result: 
     - Payment Status → Paid
     - Order Status → Delivered
     - Shipment Status → Delivered
     - Email notification logged to console

### Step 4: View Results
1. Click **Shipments** tab → See shipment marked Delivered
2. Click **Inventory** tab → See sample products
3. Check **Terminal** → See email notification output

### Optional: Test Failure Scenario
1. Create another order and add items
2. Update payment to **Failed**
   - Result: Order Canceled, Items deleted, Shipment deleted

---

## 📡 In-Memory Data Storage

**What's Included**
- Orders: Stored in array
- Order Items: Stored in array
- Inventory: Pre-populated with 4 sample products
- Shipments: Stored in array

**Sample Inventory**
```
1. Corn Seeds    - 100 units (Reorder: 20)  - Warehouse A
2. Fertilizer   - 50 units  (Reorder: 15)  - Warehouse B
3. Pesticide    - 30 units  (Reorder: 10)  - Warehouse C
4. Farm Tools   - 25 units  (Reorder: 5)   - Warehouse A
```

**Important**
- Data resets when server restarts
- No database required
- Perfect for demos and prototyping

---

## 🎨 Frontend Features

### Responsive Design
- Works on desktop, tablet, and mobile
- Flexbox and CSS Grid layouts
- Mobile-first approach
- Touch-friendly buttons

### Tab Navigation
- 4 main tabs (Orders, Items, Inventory, Shipments)
- Easy switching between sections
- Active tab highlighting
- Icons for visual clarity

### Forms & Validation
- Create order form with required fields
- Add items form with automatic calculations
- Modal dialogs for payment and stock updates
- Form error handling

### Data Display
- Clean, sortable tables
- Status badges with color coding
- Empty state messages
- Action buttons for each row

### User Feedback
- Toast notifications for success/errors
- Success checkmarks (✓)
- Warning alerts (⚠)
- Real-time data updates

---

## 🧪 Testing the API

### Using cURL (Command Line)
```bash
# Create order
curl -X POST http://localhost:3000/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName":"John Farmer","shippingAddress":"123 Farm Lane"}'

# Get all orders
curl http://localhost:3000/api/orders

# Add item to order
curl -X POST http://localhost:3000/api/orders/{ORDER_ID}/items \
  -H "Content-Type: application/json" \
  -d '{"product":"Corn Seeds","quantity":100,"unitPrice":25.50}'
```

### Using Browser Console (F12)
```javascript
// Get all orders
fetch('/api/orders').then(r => r.json()).then(d => console.log(d))

// Create order
fetch('/api/orders', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify({
    customerName: 'Jane Farmer',
    shippingAddress: '456 Farm Road'
  })
}).then(r => r.json()).then(d => console.log(d))
```

---

## 🐛 Troubleshooting

### Server Won't Start
```bash
# Verify Node.js is installed
node --version

# Navigate to correct directory
cd web

# Install dependencies
npm install

# Try again
npm start
```

### Port 3000 Already in Use
```bash
# Windows
netstat -ano | findstr :3000

# Mac/Linux
lsof -i :3000

# Kill process or use different port
PORT=3001 npm start
```

### Nothing Shows in Browser
- Hard refresh: **Ctrl+F5** (or **Cmd+Shift+R** on Mac)
- Check console: **F12 → Console**
- Verify URL: `http://localhost:3000`
- Check terminal for server logs

### API Calls Failing
- Check network tab in DevTools (F12 → Network)
- Verify server is running
- Check API endpoint URLs in app.js
- Look for CORS errors in console

### Inventory Not Loading
- Check browser console (F12)
- Verify server started successfully
- Check network tab for API calls
- Restart server and try again

---

## 📈 Email Notifications (Console Output)

When payment status changes to "Paid", console logs:
```
======================================================================
📧 EMAIL NOTIFICATION SENT
======================================================================
To: john@agriedge.com
Subject: Order ORD-0001 Payment Confirmed
----------------------------------------------------------------------
Dear John,

Your order has been received and payment confirmed!

Order Details:
  Order Number: ORD-0001
  Status: Delivered
  Total Amount: $2,550.00
  Payment Status: Paid
  Shipping Address: 123 Farm Lane
  Discounted Total: $2,295.00

Your order will be shipped shortly. Thank you for your business!

Best regards,
AgriEdge Or-Mange Ltd
======================================================================
```

---

## ✨ Key Implementation Details

### Order Number Generation
```javascript
// Format: ORD-0001, ORD-0002, etc.
function generateOrderNumber() {
  return `ORD-${String(orderCounter++).padStart(4, '0')}`;
}
```

### Total Calculation
```javascript
// Sum of all item totals for an order
const newTotal = itemsForOrder.reduce((sum, i) => sum + i.totalPrice, 0);
order.totalAmount = newTotal;
```

### Stock Status
```javascript
// Low if stock <= reorderLevel
const status = stockQuantity <= reorderLevel ? 'Low' : 'Sufficient';
```

### Order Status Transitions
```
New → (add item) → Processing
Processing → (payment Paid) → Delivered
Processing → (payment Failed) → Canceled
```

---

## 🚀 Running on Different Ports

### Windows
```batch
set PORT=3001
npm start
```

### Mac/Linux
```bash
export PORT=3001
npm start

# Or inline
PORT=3001 npm start
```

Then visit: `http://localhost:3001`

---

## 📚 Additional Documentation

- **[web/README.md](web/README.md)** - Comprehensive web app documentation
- **[WEB_QUICKSTART.md](WEB_QUICKSTART.md)** - Quick start guide
- **[README.md](README.md)** - Main project documentation
- **[CLI_QUICKSTART.md](CLI_QUICKSTART.md)** - CLI version guide

---

## 🎓 Learning Resources

### Concepts Covered
- REST API design
- Express.js server
- Client-server communication
- In-memory data structures
- HTML/CSS/JavaScript frontend
- Fetch API for requests
- Tab interfaces
- Modal dialogs
- Form handling
- Data validation

### Technologies Used
- **Backend**: Node.js, Express, body-parser
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Storage**: JavaScript arrays and objects
- **Communication**: REST API with JSON

---

## 🎉 You're All Set!

Your AgriEdge OMS web application is ready to use. Simply:

1. **Install**: `npm install` (one time)
2. **Start**: `npm start`
3. **Open**: `http://localhost:3000`
4. **Demo**: Follow the workflow steps above

---

## 📞 Quick Reference

| Task | Command |
|------|---------|
| Install | `npm install` |
| Start | `npm start` |
| Kill Server | Ctrl+C |
| View API | `http://localhost:3000/api/orders` |
| Open Dev Tools | F12 |
| Force Refresh | Ctrl+F5 |
| Check Node Version | `node --version` |

---

**Enjoy your AgriEdge OMS web application!** 🌾

If you have issues, check [web/README.md](web/README.md) for more detailed troubleshooting.

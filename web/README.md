# AgriEdge OMS - Web Application

A full-stack web application for the AgriEdge Order Management System built with Node.js + Express backend and vanilla HTML/CSS/JS frontend.

---

## 🚀 Quick Start

### System Requirements
- **Node.js**: Version 12.0.0 or higher
- **npm**: Version 6.0.0 or higher (comes with Node.js)
- **Browser**: Modern browser (Chrome, Firefox, Safari, Edge)

### Installation & Setup

1. **Navigate to the web directory:**
   ```bash
   cd AgriEdge-OMS/web
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```
   This installs Express and body-parser packages.

3. **Start the server:**
   ```bash
   npm start
   ```
   or
   ```bash
   node server.js
   ```

4. **Open in browser:**
   ```
   http://localhost:3000
   ```

You should see the AgriEdge OMS interface with a green header and tab navigation.

---

## 📁 Project Structure

```
web/
├── server.js              # Express backend with REST API
├── package.json           # Node.js dependencies and config
├── public/
│   ├── index.html         # Frontend UI with tab interface
│   ├── style.css          # Responsive styling
│   └── app.js             # Frontend JavaScript logic
└── README.md              # This file
```

---

## 🌐 Web Interface

### Tabs

#### 1. **Orders Tab** 📋
- **Create Order Form**: Enter customer name and shipping address
- **Auto-generated Order Number**: ORD-0001, ORD-0002, etc.
- **Orders Table**: View all orders with status, payment, and total
- **Update Payment**: Change payment status for any order

#### 2. **Items Tab** 📦
- **Add Order Item Form**: Select order, enter product, quantity, unit price
- **Auto-calculation**: Total price = Quantity × Unit Price
- **Order Items Preview**: See all items for selected order
- **Auto-updates**: Order status updates to Processing, total recalculated

#### 3. **Inventory Tab** 📊
- **View All Products**: Pre-populated with sample inventory
- **Stock Status**: Shows "Low" or "Sufficient" status
- **Update Stock**: Edit quantity for any product
- **Reorder Alerts**: Warning when stock falls below reorder level

#### 4. **Shipments Tab** 🚚
- **Shipment List**: View all active shipments
- **Tracking Info**: Tracking number, carrier, and status
- **Auto-created**: Shipments created automatically when items are added
- **Status Updates**: Delivered when payment is marked as Paid

---

## 🔌 REST API Endpoints

### Orders
- `POST /api/orders` - Create new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/:id` - Get single order
- `PATCH /api/orders/:id/payment` - Update payment status

### Order Items
- `POST /api/orders/:id/items` - Add order item

### Inventory
- `GET /api/inventory` - Get all inventory items
- `PATCH /api/inventory/:id` - Update stock quantity

### Shipments
- `GET /api/shipments` - Get all shipments

---

## 🎯 Demo Workflow

Follow these steps to test the complete system:

### Step 1: Create an Order
1. Click **Orders** tab
2. Click **+ New Order** button
3. Enter customer name: `John Farmer`
4. Enter shipping address: `123 Agriculture Lane, Farm City`
5. Click **Create Order**
   - ✓ Order created: ORD-0001

### Step 2: Add Order Items
1. Click **Items** tab
2. Select order: `ORD-0001 - John Farmer`
3. Enter product: `Corn Seeds`
4. Enter quantity: `100`
5. Enter unit price: `25.50`
6. Click **Add Item to Order**
   - ✓ Item added
   - ✓ Order status → Processing
   - ✓ Shipment created automatically

### Step 3: Update Payment Status
1. Click **Orders** tab
2. Find ORD-0001 row
3. Click **Update Payment** button
4. Select: `Paid`
5. Click **Update**
   - ✓ Payment status → Paid
   - ✓ Order status → Delivered
   - ✓ Shipment status → Delivered
   - ✓ Email notification sent to console

### Step 4: View Shipments
1. Click **Shipments** tab
2. See ORD-0001 shipment with:
   - Tracking number: TEST_{orderId}
   - Carrier: Local Courier
   - Status: Delivered

### Step 5: View Inventory
1. Click **Inventory** tab
2. See all products with stock status
3. Try updating stock for a product

### Step 6: Test Payment Failure (Optional)
1. Create another order
2. Add items
3. Update payment to **Failed**
   - ✓ Order status → Canceled
   - ✓ Items deleted
   - ✓ Shipment deleted
   - ✓ Cancellation email sent

---

## 📊 In-Memory Storage

The application uses in-memory data storage:
- No database required
- Data resets when server restarts
- All data stored in Node.js arrays/objects
- Perfect for testing and demonstrations

**Sample Inventory (Auto-loaded):**
- Corn Seeds (Stock: 100, Reorder: 20)
- Fertilizer (Stock: 50, Reorder: 15)
- Pesticide (Stock: 30, Reorder: 10)
- Farm Tools (Stock: 25, Reorder: 5)

---

## 🎨 Frontend Features

### Responsive Design
- Works on desktop, tablet, and mobile
- Tab-based navigation for easy switching
- Modern gradient header
- Clean, professional styling

### User Feedback
- Toast notifications for success/error messages
- Visual status badges for orders and payments
- Real-time table updates
- Modal dialogs for forms

### Data Tables
- Sortable columns (with enhancement)
- Color-coded status indicators
- Action buttons for each row
- Empty state messages

---

## 🖥️ Backend Features

### Express Server
- RESTful API design
- JSON request/response
- Body parser middleware
- Static file serving

### Business Logic
- Auto-generated order numbers
- Total amount aggregation from items
- Payment status-based order status updates
- Cascade deletion on payment failure
- Shipment auto-creation
- Stock status calculation

### Email Simulation
- Console output for email notifications
- Triggered on payment status changes
- Includes order details and discounted total
- Both payment confirmed and cancellation emails

---

## 🧪 Testing the API with cURL

### Create an Order
```bash
curl -X POST http://localhost:3000/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName":"John Farmer","shippingAddress":"123 Agri Lane"}'
```

### Get All Orders
```bash
curl http://localhost:3000/api/orders
```

### Add Order Item
```bash
curl -X POST http://localhost:3000/api/orders/{ORDER_ID}/items \
  -H "Content-Type: application/json" \
  -d '{"product":"Corn Seeds","quantity":100,"unitPrice":25.50}'
```

### Update Payment Status
```bash
curl -X PATCH http://localhost:3000/api/orders/{ORDER_ID}/payment \
  -H "Content-Type: application/json" \
  -d '{"paymentStatus":"Paid"}'
```

---

## 🐛 Troubleshooting

### Port Already in Use
If port 3000 is already in use:
```bash
# Change port in server.js or use environment variable
PORT=3001 npm start
# Then visit http://localhost:3001
```

### Dependencies Not Installed
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
npm start
```

### Frontend Not Loading
- Clear browser cache (Ctrl+F5 or Cmd+Shift+R)
- Check browser console for errors (F12)
- Verify server is running on port 3000

### CORS Issues
- Ensure frontend is served from `http://localhost:3000`
- API endpoints use same origin (no CORS headers needed)

---

## 📝 Key Implementation Details

### Order Number Generation
- Format: ORD-0001, ORD-0002, etc.
- Counter incremented on each order creation
- Formatted with leading zeros

### Total Amount Calculation
- `Total = SUM(Quantity × Unit Price)` for all items
- Updated when items are added
- Auto-set payment status based on total

### Stock Status Formula
```javascript
status = stockQuantity <= reorderLevel ? "Low" : "Sufficient"
```

### Order Status Transitions
```
New → (item added) → Processing
Processing → (payment Paid) → Delivered
Processing → (payment Failed) → Canceled
```

### Email Notifications (Console Output)
- Triggered on payment status changes
- Includes order details (number, status, total, discount)
- Simulates real email format
- Both paid and cancelled scenarios

---

## 🚀 Future Enhancements

- [ ] Database integration (MongoDB, PostgreSQL)
- [ ] Authentication and user roles
- [ ] Persistent data storage
- [ ] Advanced filtering and search
- [ ] Order reports and analytics
- [ ] Email functionality (Nodemailer)
- [ ] File uploads (invoices, documents)
- [ ] Real-time updates (WebSockets)
- [ ] API documentation (Swagger)
- [ ] Unit and integration tests

---

## 📜 File Descriptions

### **server.js**
- Express.js server setup on port 3000
- In-memory storage for orders, items, inventory, shipments
- 7 REST API endpoints
- Business logic implementation
- Middleware for body parsing and static files
- Console logging for order operations

### **public/index.html**
- Tab-based UI with 4 main sections
- Forms for creating orders and items
- Tables for displaying data
- Modal dialogs for payment and stock updates
- Responsive layout
- No external CSS/JS dependencies (vanilla only)

### **public/style.css**
- Modern gradient styling
- Tab navigation system
- Form styling with validation feedback
- Table styling with status badges
- Modal dialogs and overlays
- Responsive design (mobile-first)
- CSS Grid and Flexbox layouts

### **public/app.js**
- DOM manipulation and event handling
- API calls using Fetch API
- Data rendering functions
- Modal management
- Toast notifications
- Tab switching logic
- Form validation and submission

### **package.json**
- Project metadata
- Dependencies: express, body-parser
- Scripts for starting server
- Engine requirements (Node.js >= 12)

---

## 💡 How It Works

1. **Server Initialization** → Express server starts on port 3000
2. **Static Files Served** → index.html, style.css, app.js loaded
3. **Inventory Loaded** → Sample products initialized in memory
4. **Frontend Loads** → JavaScript app.js runs in browser
5. **API Communication** → Frontend uses Fetch API to call backend
6. **Data Updates** → Server modifies in-memory data and responds
7. **UI Renders** → Frontend updates tables and forms with new data

---

## 👥 Support

For issues or questions:
1. Check the logs in browser console (F12)
2. Check server console for API logs
3. Verify all files are in correct directories
4. Try restarting the server

---

## 📄 License

This project is part of the AgriEdge-OMS initiative.

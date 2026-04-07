# AgriEdge OMS - Web App Quick Start

A full-stack web application built with Node.js + Express (backend) and Vanilla HTML/CSS/JavaScript (frontend).

## 🚀 Getting Started

### Prerequisites
- **Node.js** (v12+) - [Download](https://nodejs.org/)
- **npm** (comes with Node.js)
- Web browser (Chrome, Firefox, Edge, Safari)

### Installation

```bash
# Navigate to the web directory
cd web

# Install dependencies
npm install

# Start the server
npm start

# Or on Windows, run:
start.bat

# Open browser to:
http://localhost:3000
```

**That's it!** The application should load in your browser.

---

## 📋 What You'll See

### Landing Screen
- Green header: "AgriEdge Or-Mange Ltd - Order Management System"
- 4 tabs: Orders, Items, Inventory, Shipments
- In-memory storage with sample inventory loaded

### Features
1. **Create Orders** - Auto-numbered (ORD-0001, ORD-0002, etc.)
2. **Add Items** - Calculate totals, auto-create shipments
3. **Update Payments** - Triggers order status & email notifications
4. **View Inventory** - Track stock levels and reorder status
5. **Track Shipments** - Automatic shipment creation and updates

---

## 🎯 Quick Demo (2 minutes)

1. **Create an Order**
   - Click "Orders" tab
   - Click "+ New Order"
   - Enter: Name = "John Farmer", Address = "123 Farm Lane"
   - Click "Create Order" → You see ORD-0001

2. **Add Items**
   - Click "Items" tab
   - Select ORD-0001
   - Enter: Product = "Corn Seeds", Qty = 100, Price = 25.50
   - Click "Add Item" → Order moves to Processing

3. **Update Payment**
   - Click "Orders" tab
   - Click "Update Payment" on ORD-0001
   - Select "Paid" → Order moves to Delivered, email logged

4. **View Results**
   - Check "Shipments" tab → Shipment now Delivered
   - Check "Inventory" tab → See sample products
   - Check server console → Email notification output

---

## 📁 Project Structure

```
web/
├── server.js              (Express backend with REST API)
├── package.json          (Node dependencies)
├── start.bat             (Quick start script)
├── README.md             (Full documentation)
├── public/
│   ├── index.html        (Tab-based UI)
│   ├── style.css         (Responsive styling)
│   └── app.js            (Frontend logic)
└── node_modules/         (Installed packages)
```

---

## 🔧 Troubleshooting

**Port 3000 already in use?**
```bash
Set NODE_ENV=production
Set PORT=3001
npm start
# Then visit http://localhost:3001
```

**Dependencies not installed?**
```bash
rm -rf node_modules package-lock.json
npm install
npm start
```

**Frontend not loading?**
- Hard refresh: Ctrl+F5 (or Cmd+Shift+R on Mac)
- Check browser console: F12 → Console tab
- Check server is running: `http://localhost:3000`

---

## 🎨 UI Features

- ✅ Responsive design (mobile, tablet, desktop)
- ✅ Tab-based navigation
- ✅ Toast notifications for feedback
- ✅ Status badges with color coding
- ✅ Modal dialogs for forms
- ✅ Data tables with actions
- ✅ Form validation

---

## 📡 API Endpoints

All endpoints return JSON. Base URL: `http://localhost:3000/api`

```
POST   /orders              Create order
GET    /orders              List all orders
GET    /orders/:id          Get single order
PATCH  /orders/:id/payment  Update payment status
POST   /orders/:id/items    Add order item

GET    /inventory           List inventory
PATCH  /inventory/:id       Update stock

GET    /shipments           List shipments
```

---

## 💾 In-Memory Storage

Data resets on server restart. Perfect for:
- ✅ Demonstrations
- ✅ Prototyping
- ✅ Testing workflows
- ✅ No database setup needed

Sample inventory auto-loaded:
- Corn Seeds (100 units)
- Fertilizer (50 units)
- Pesticide (30 units)
- Farm Tools (25 units)

---

## 🎬 Demo Workflow Flowchart

```
┌─────────────────┐
│  Create Order   │ → ORD-0001 (Status: New)
└────────┬────────┘
         │
         ▼
┌──────────────────┐
│  Add Items       │ → Total: $2,550
│ (e.g., 100x@25) │ → Status: Processing
└────────┬────────┘ → Shipment Created
         │
         ▼
┌──────────────────┐
│ Update Payment   │
│  to "Paid"       │ → Status: Delivered
└────────┬────────┘ → Shipment: Delivered
         │          → Email Sent ✓
         ▼
┌──────────────────┐
│  Order Complete  │
└──────────────────┘
```

---

## 📊 Console Logs

Check your terminal for:
- ✓ Order creation messages
- ✓ Item additions
- ✓ Payment updates
- 📧 Email notifications (mocked)
- ⚠️ Warning messages

---

## 🔗 Full Documentation

For complete details, see:
- [web/README.md](web/README.md) - Comprehensive guide
- [README.md](README.md) - Project overview
- [CLI_QUICKSTART.md](CLI_QUICKSTART.md) - CLI version

---

## ✨ Key Features Implemented

✅ Auto-generated order numbers  
✅ Order status workflow  
✅ Payment status management  
✅ Item total aggregation  
✅ Inventory stock tracking  
✅ Automatic shipment creation  
✅ Cascade deletion on payment failure  
✅ Email notifications (console output)  
✅ Validation rules  
✅ Responsive web UI  
✅ RESTful API  
✅ In-memory data storage  

---

## 🚀 Next Steps

1. ✅ Try the demo workflow above
2. ✅ Explore all 4 tabs
3. ✅ Check server console for logs
4. ✅ Try creating multiple orders
5. ✅ Test payment failure scenario
6. ✅ Update inventory stocks

---

## 💡 Tips

- Use browser DevTools (F12) to see API calls in Network tab
- Check Application tab for local state
- Use browser console to see any JavaScript errors
- Check terminal for server logs and email output
- Refresh page if data doesn't appear

---

**Enjoy exploring AgriEdge OMS!** 🌾


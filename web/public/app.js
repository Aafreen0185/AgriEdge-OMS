// API Base URL
const API_BASE = 'http://localhost:3000/api';

// Data
let orders = [];
let inventory = [];
let shipments = [];
let selectedOrderId = null;
let selectedInventoryId = null;

// ============== DOM Elements ==============
const orderForm = document.getElementById('orderForm');
const itemForm = document.getElementById('itemForm');
const stockForm = document.getElementById('stockForm');
const paymentForm = document.getElementById('paymentForm');
const ordersTable = document.getElementById('ordersTable');
const ordersBody = document.getElementById('ordersBody');
const inventoryBody = document.getElementById('inventoryBody');
const shipmentsBody = document.getElementById('shipmentsBody');
const orderSelect = document.getElementById('orderSelect');
const itemsPreview = document.getElementById('itemsPreview');
const toast = document.getElementById('toast');

// Modal Elements
const createOrderForm = document.getElementById('createOrderForm');
const stockModal = document.getElementById('stockModal');
const paymentModal = document.getElementById('paymentModal');
const newOrderBtn = document.getElementById('newOrderBtn');
const cancelOrderBtn = document.getElementById('cancelOrderBtn');
const closeStockModal = document.getElementById('closeStockModal');
const cancelStockBtn = document.getElementById('cancelStockBtn');
const closePaymentModal = document.getElementById('closePaymentModal');
const cancelPaymentBtn = document.getElementById('cancelPaymentBtn');

// Tab Elements
const tabButtons = document.querySelectorAll('.tab-button');
const tabContents = document.querySelectorAll('.tab-content');

// ============== EVENT LISTENERS ==============

// Tab Navigation
tabButtons.forEach(button => {
    button.addEventListener('click', () => {
        const tabName = button.getAttribute('data-tab');
        switchTab(tabName);
    });
});

// Order Form
newOrderBtn.addEventListener('click', () => {
    createOrderForm.classList.remove('hidden');
    document.getElementById('customerName').focus();
});

cancelOrderBtn.addEventListener('click', () => {
    createOrderForm.classList.add('hidden');
    orderForm.reset();
});

orderForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const customerName = document.getElementById('customerName').value;
    const shippingAddress = document.getElementById('shippingAddress').value;

    try {
        const response = await fetch(`${API_BASE}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ customerName, shippingAddress })
        });

        if (!response.ok) throw new Error('Failed to create order');

        showToast('✓ Order created successfully!', 'success');
        orderForm.reset();
        createOrderForm.classList.add('hidden');
        loadOrders();
        loadOrderSelect();
    } catch (error) {
        showToast(`Error: ${error.message}`, 'error');
    }
});

// Item Form
itemForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const orderId = document.getElementById('orderSelect').value;
    const product = document.getElementById('productName').value;
    const quantity = parseInt(document.getElementById('quantity').value);
    const unitPrice = parseFloat(document.getElementById('unitPrice').value);

    if (!orderId) {
        showToast('Please select an order', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/orders/${orderId}/items`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ product, quantity, unitPrice })
        });

        if (!response.ok) throw new Error('Failed to add item');

        showToast(`✓ Item added: ${product}`, 'success');
        itemForm.reset();
        loadOrders();
        updateItemsPreview(orderId);
    } catch (error) {
        showToast(`Error: ${error.message}`, 'error');
    }
});

// Stock Form
stockForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const newStock = parseInt(document.getElementById('newStock').value);

    try {
        const response = await fetch(`${API_BASE}/inventory/${selectedInventoryId}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stockQuantity: newStock })
        });

        if (!response.ok) throw new Error('Failed to update stock');

        showToast('✓ Stock updated successfully!', 'success');
        stockModal.classList.add('hidden');
        stockForm.reset();
        loadInventory();
    } catch (error) {
        showToast(`Error: ${error.message}`, 'error');
    }
});

// Payment Form
paymentForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const paymentStatus = document.getElementById('paymentStatus').value;

    try {
        const response = await fetch(`${API_BASE}/orders/${selectedOrderId}/payment`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ paymentStatus })
        });

        if (!response.ok) throw new Error('Failed to update payment status');

        showToast(`✓ Payment status updated to ${paymentStatus}!`, 'success');
        paymentModal.classList.add('hidden');
        paymentForm.reset();
        loadOrders();
    } catch (error) {
        showToast(`Error: ${error.message}`, 'error');
    }
});

// Modal Close Buttons
closeStockModal.addEventListener('click', () => stockModal.classList.add('hidden'));
cancelStockBtn.addEventListener('click', () => stockModal.classList.add('hidden'));
closePaymentModal.addEventListener('click', () => paymentModal.classList.add('hidden'));
cancelPaymentBtn.addEventListener('click', () => paymentModal.classList.add('hidden'));

// Order Select Change
orderSelect.addEventListener('change', (e) => {
    if (e.target.value) {
        updateItemsPreview(e.target.value);
    }
});

// ============== TAB SWITCHING ==============
function switchTab(tabName) {
    // Update buttons
    tabButtons.forEach(btn => btn.classList.remove('active'));
    event.target.closest('.tab-button').classList.add('active');

    // Update content
    tabContents.forEach(content => content.classList.remove('active'));
    document.getElementById(tabName).classList.add('active');

    // Load data for current tab
    if (tabName === 'orders') loadOrders();
    if (tabName === 'items') loadOrderSelect();
    if (tabName === 'inventory') loadInventory();
    if (tabName === 'shipments') loadShipments();
}

// ============== API CALLS ==============

async function loadOrders() {
    try {
        const response = await fetch(`${API_BASE}/orders`);
        if (!response.ok) throw new Error('Failed to load orders');
        orders = await response.json();
        renderOrders();
    } catch (error) {
        console.error(error);
    }
}

async function loadInventory() {
    try {
        const response = await fetch(`${API_BASE}/inventory`);
        if (!response.ok) throw new Error('Failed to load inventory');
        inventory = await response.json();
        renderInventory();
    } catch (error) {
        console.error(error);
    }
}

async function loadShipments() {
    try {
        const response = await fetch(`${API_BASE}/shipments`);
        if (!response.ok) throw new Error('Failed to load shipments');
        shipments = await response.json();
        renderShipments();
    } catch (error) {
        console.error(error);
    }
}

async function loadOrderSelect() {
    try {
        const response = await fetch(`${API_BASE}/orders`);
        if (!response.ok) throw new Error('Failed to load orders');
        const data = await response.json();
        
        orderSelect.innerHTML = '<option value="">-- Choose an order --</option>';
        data.forEach(order => {
            const option = document.createElement('option');
            option.value = order.id;
            option.textContent = `${order.orderNumber} - ${order.customerName}`;
            orderSelect.appendChild(option);
        });
    } catch (error) {
        console.error(error);
    }
}

async function updateItemsPreview(orderId) {
    try {
        const response = await fetch(`${API_BASE}/orders/${orderId}`);
        if (!response.ok) throw new Error('Failed to load order');
        const order = await response.json();

        if (order.items.length === 0) {
            itemsPreview.innerHTML = '<p class="empty-state">No items added yet</p>';
            return;
        }

        itemsPreview.innerHTML = order.items.map(item => `
            <div class="item-card">
                <div class="item-info">
                    <div class="item-product">${item.product}</div>
                    <div class="item-details">
                        ${item.quantity}x @ $${item.unitPrice.toFixed(2)} = $${item.totalPrice.toFixed(2)}
                    </div>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error(error);
    }
}

// ============== RENDERING FUNCTIONS ==============

function renderOrders() {
    if (orders.length === 0) {
        ordersBody.innerHTML = '<tr class="empty-state"><td colspan="7">No orders yet. Create one to get started!</td></tr>';
        return;
    }

    ordersBody.innerHTML = orders.map(order => `
        <tr>
            <td><strong>${order.orderNumber}</strong></td>
            <td>${order.customerName}</td>
            <td><span class="status-badge status-${order.orderStatus.toLowerCase()}">${order.orderStatus}</span></td>
            <td><span class="status-badge status-${order.paymentStatus.toLowerCase()}">${order.paymentStatus}</span></td>
            <td>$${order.totalAmount.toFixed(2)}</td>
            <td>${order.shippingAddress.substring(0, 20)}...</td>
            <td>
                <button class="btn btn-small btn-primary" onclick="openPaymentModal('${order.id}')">Update Payment</button>
            </td>
        </tr>
    `).join('');
}

function renderInventory() {
    if (inventory.length === 0) {
        inventoryBody.innerHTML = '<tr class="empty-state"><td colspan="6">No inventory items</td></tr>';
        return;
    }

    inventoryBody.innerHTML = inventory.map(item => `
        <tr>
            <td><strong>${item.product}</strong></td>
            <td>${item.stockQuantity}</td>
            <td>${item.reorderLevel}</td>
            <td><span class="status-badge status-${item.status.toLowerCase()}">${item.status}</span></td>
            <td>${item.warehouseLocation}</td>
            <td>
                <button class="btn btn-small btn-secondary" onclick="openStockModal('${item.id}', '${item.product}', ${item.stockQuantity})">Update Stock</button>
            </td>
        </tr>
    `).join('');
}

function renderShipments() {
    if (shipments.length === 0) {
        shipmentsBody.innerHTML = '<tr class="empty-state"><td colspan="5">No shipments yet</td></tr>';
        return;
    }

    shipmentsBody.innerHTML = shipments.map(shipment => `
        <tr>
            <td><strong>${shipment.orderNumber}</strong></td>
            <td>${shipment.customerName}</td>
            <td>${shipment.trackingNumber}</td>
            <td>${shipment.carrier}</td>
            <td><span class="status-badge status-${shipment.status.toLowerCase()}">${shipment.status}</span></td>
        </tr>
    `).join('');
}

// ============== MODAL FUNCTIONS ==============

function openPaymentModal(orderId) {
    selectedOrderId = orderId;
    paymentModal.classList.remove('hidden');
}

function openStockModal(inventoryId, product, currentStock) {
    selectedInventoryId = inventoryId;
    document.getElementById('newStock').value = currentStock;
    stockModal.classList.remove('hidden');
}

// ============== UTILITY FUNCTIONS ==============

function showToast(message, type = 'success') {
    toast.textContent = message;
    toast.className = `toast ${type}`;
    toast.classList.remove('hidden');

    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

// ============== INITIALIZATION ==============

function init() {
    loadOrders();
    loadInventory();
}

// Load data on page load
document.addEventListener('DOMContentLoaded', init);

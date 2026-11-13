/**
 * Mini ERP – Client-side JavaScript
 * Handles: sidebar toggle, sale form dynamics, delete confirm, AJAX price fetch
 */

/* ----------------------------------------------------------------
   Sidebar Toggle
   ---------------------------------------------------------------- */
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const main    = document.getElementById('mainContent');
    if (!sidebar) return;

    if (window.innerWidth <= 768) {
        sidebar.classList.toggle('mobile-open');
    } else {
        sidebar.classList.toggle('collapsed');
        main.classList.toggle('sidebar-collapsed');
    }
}

/* ----------------------------------------------------------------
   Auto-dismiss alerts after 5 seconds
   ---------------------------------------------------------------- */
document.addEventListener('DOMContentLoaded', function () {
    const alerts = document.querySelectorAll('.erp-alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 6000);
    });
});

/* ----------------------------------------------------------------
   Sale Form – dynamic product rows
   ---------------------------------------------------------------- */
let saleRowCount = 0;
const contextPath = document.documentElement.dataset.context || '';

function addSaleRow() {
    const products = window.erpProducts || [];
    if (products.length === 0) return;

    saleRowCount++;
    const container = document.getElementById('saleItemsContainer');
    if (!container) return;

    const rowDiv = document.createElement('div');
    rowDiv.className = 'sale-item-row';
    rowDiv.id = 'saleRow_' + saleRowCount;

    let optionsHtml = '<option value="">-- Select Product --</option>';
    products.forEach(function (p) {
        optionsHtml += `<option value="${p.id}" data-price="${p.price}" data-stock="${p.quantity}">
            ${escHtml(p.name)} (Stock: ${p.quantity})
        </option>`;
    });

    rowDiv.innerHTML = `
        <div class="row g-2 align-items-end">
            <div class="col-md-5">
                <label class="form-label">Product</label>
                <select name="productIds" class="form-select product-select"
                        onchange="onProductChange(this, ${saleRowCount})" required>
                    ${optionsHtml}
                </select>
            </div>
            <div class="col-md-2">
                <label class="form-label">Unit Price (₹)</label>
                <input type="text" id="price_${saleRowCount}" class="form-control" readonly placeholder="0.00">
            </div>
            <div class="col-md-2">
                <label class="form-label">Qty</label>
                <input type="number" name="quantities" id="qty_${saleRowCount}" class="form-control"
                       min="1" value="1" onchange="recalcRow(${saleRowCount})" oninput="recalcRow(${saleRowCount})" required>
            </div>
            <div class="col-md-2">
                <label class="form-label">Subtotal (₹)</label>
                <input type="text" id="subtotal_${saleRowCount}" class="form-control sale-item-total" readonly placeholder="0.00">
            </div>
            <div class="col-md-1 d-flex align-items-end">
                <button type="button" class="btn btn-sm btn-danger-erp w-100"
                        onclick="removeSaleRow(${saleRowCount})" title="Remove">
                    <i class="bi bi-trash"></i>
                </button>
            </div>
        </div>
        <div class="row mt-1">
            <div class="col-12">
                <small id="stockHint_${saleRowCount}" class="form-text text-muted"></small>
            </div>
        </div>
    `;
    container.appendChild(rowDiv);
}

function removeSaleRow(rowId) {
    const row = document.getElementById('saleRow_' + rowId);
    if (row) { row.remove(); }
    updateGrandTotal();
}

function onProductChange(selectEl, rowId) {
    const option = selectEl.options[selectEl.selectedIndex];
    const price  = parseFloat(option.dataset.price) || 0;
    const stock  = parseInt(option.dataset.stock) || 0;

    document.getElementById('price_' + rowId).value = formatNumber(price);
    document.getElementById('qty_'   + rowId).max   = stock;
    document.getElementById('stockHint_' + rowId).textContent =
        price > 0 ? `Available stock: ${stock} units` : '';

    recalcRow(rowId);
}

function recalcRow(rowId) {
    const priceStr = document.getElementById('price_' + rowId).value.replace(/,/g, '');
    const price    = parseFloat(priceStr) || 0;
    const qty      = parseInt(document.getElementById('qty_' + rowId).value) || 0;
    const subtotal = price * qty;
    document.getElementById('subtotal_' + rowId).value = formatNumber(subtotal);
    updateGrandTotal();
}

function updateGrandTotal() {
    const subtotalFields = document.querySelectorAll('[id^="subtotal_"]');
    let total = 0;
    subtotalFields.forEach(function (el) {
        total += parseFloat(el.value.replace(/,/g, '')) || 0;
    });
    const display = document.getElementById('grandTotalDisplay');
    if (display) display.textContent = '₹' + formatNumber(total);
}

function formatNumber(n) {
    // Indian number formatting
    return new Intl.NumberFormat('en-IN', {
        minimumFractionDigits: 2, maximumFractionDigits: 2
    }).format(n);
}

function escHtml(str) {
    const d = document.createElement('div');
    d.textContent = str;
    return d.innerHTML;
}

/* ----------------------------------------------------------------
   Delete Confirmation
   ---------------------------------------------------------------- */
function confirmDelete(formId, productName) {
    if (confirm('Are you sure you want to delete "' + productName + '"?\nThis action cannot be undone.')) {
        document.getElementById(formId).submit();
    }
}

/* ----------------------------------------------------------------
   Sale form validation before submit
   ---------------------------------------------------------------- */
function validateSaleForm() {
    const customerSelect = document.getElementById('customerId');
    if (!customerSelect || !customerSelect.value) {
        alert('Please select a customer.');
        return false;
    }
    const rows = document.querySelectorAll('.sale-item-row');
    if (rows.length === 0) {
        alert('Please add at least one product.');
        return false;
    }
    let hasValid = false;
    for (const row of rows) {
        const sel = row.querySelector('select[name="productIds"]');
        const qty = row.querySelector('input[name="quantities"]');
        if (sel && sel.value && qty && parseInt(qty.value) > 0) {
            hasValid = true;
            break;
        }
    }
    if (!hasValid) {
        alert('Please select at least one product with a valid quantity.');
        return false;
    }
    return true;
}

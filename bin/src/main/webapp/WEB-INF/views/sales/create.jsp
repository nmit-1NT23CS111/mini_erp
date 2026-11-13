<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Create Sale" scope="request"/>
<c:set var="activeMenu" value="create-sale" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-cart-plus me-2 text-primary"></i>Create New Sale</h1>
        <p>Select a customer and add products for the invoice.</p>
    </div>
</div>

<div class="erp-card">
    <div class="erp-card-header">
        <h5><i class="bi bi-receipt me-2"></i>Sale Details</h5>
    </div>
    <div class="erp-card-body">
        <form method="post" action="${pageContext.request.contextPath}/sales/save" onsubmit="return validateSaleForm();">
            <div class="row g-3 mb-4">
                <div class="col-lg-6">
                    <label for="customerId" class="form-label">Customer <span class="text-danger">*</span></label>
                    <select id="customerId" name="customerId" class="form-select" required>
                        <option value="">-- Select customer --</option>
                        <c:forEach var="customer" items="${customers}">
                            <option value="${customer.id}">${customer.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-lg-6 d-flex align-items-end justify-content-end">
                    <button type="button" class="btn-primary-erp" onclick="addSaleRow()" id="btn-add-sale-row">
                        <i class="bi bi-plus-lg me-1"></i>Add Product
                    </button>
                </div>
            </div>

            <div id="saleItemsContainer">
                <div class="text-muted small">No products selected yet.</div>
            </div>

            <hr class="my-4">

            <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
                <div>
                    <small class="text-uppercase text-muted fw-semibold">Grand Total</small>
                    <div class="fs-3 fw-bold text-primary" id="grandTotalDisplay">₹0.00</div>
                </div>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/sales" class="btn-secondary-erp" id="btn-cancel-sale">
                        <i class="bi bi-x me-1"></i>Cancel
                    </a>
                    <button type="submit" class="btn-primary-erp" id="btn-submit-sale">
                        <i class="bi bi-check-lg me-1"></i>Create Sale
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    window.erpProducts = [
        <c:forEach var="product" items="${products}" varStatus="status">
            {
                id: ${product.id},
                name: "<c:out value='${product.name}'/>",
                price: ${product.price},
                quantity: ${product.quantity}
            }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
</script>

<%@ include file="../layout/footer.jsp" %>

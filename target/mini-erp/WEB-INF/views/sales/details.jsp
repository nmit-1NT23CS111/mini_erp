<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Sale Details" scope="request"/>
<c:set var="activeMenu" value="sales" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-receipt me-2 text-primary"></i>Sale #${sale.id}</h1>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sales">Sales</a></li>
                <li class="breadcrumb-item active">Sale #${sale.id}</li>
            </ol>
        </nav>
    </div>
    <div>
        <a href="${pageContext.request.contextPath}/sales" class="btn-secondary-erp" id="btn-back-sales">
            <i class="bi bi-arrow-left"></i> Back to Sales
        </a>
    </div>
</div>

<div class="row g-3 mb-3">
    <div class="col-md-4">
        <div class="erp-card h-100">
            <div class="erp-card-header">
                <h5><i class="bi bi-person me-2"></i>Customer</h5>
            </div>
            <div class="erp-card-body">
                <div class="fw-semibold fs-5">${sale.customerName}</div>
                <div class="text-muted small">Customer ID: ${sale.customerId}</div>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="erp-card h-100">
            <div class="erp-card-header">
                <h5><i class="bi bi-calendar3 me-2"></i>Date</h5>
            </div>
            <div class="erp-card-body">
                <div class="fw-semibold">${sale.saleDate}</div>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="erp-card h-100">
            <div class="erp-card-header">
                <h5><i class="bi bi-currency-rupee me-2"></i>Total</h5>
            </div>
            <div class="erp-card-body">
                <div class="fw-bold fs-4 text-primary">
                    ₹<fmt:formatNumber value="${sale.totalAmount}" pattern="#,##,##0.00"/>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="erp-card">
    <div class="erp-card-header">
        <h5><i class="bi bi-list-check me-2"></i>Items Sold</h5>
    </div>
    <div class="table-responsive">
        <table class="erp-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th class="text-center">Qty</th>
                    <th class="text-end">Unit Price</th>
                    <th class="text-end">Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty sale.items}">
                        <tr>
                            <td colspan="4" class="table-empty">
                                <i class="bi bi-box-seam"></i>
                                No items available for this sale.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${sale.items}">
                            <tr>
                                <td class="fw-600">${item.productName}</td>
                                <td class="text-center">${item.quantity}</td>
                                <td class="text-end">₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##,##0.00"/></td>
                                <td class="text-end">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##,##0.00"/></td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Dashboard" scope="request"/>
<c:set var="activeMenu" value="dashboard" scope="request"/>
<%@ include file="layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-speedometer2 me-2 text-primary"></i>Dashboard</h1>
        <p>Welcome back, <strong>${sessionScope.loggedInUser}</strong>! Here's your business overview.</p>
    </div>
    <div>
        <a href="${pageContext.request.contextPath}/sales/create" class="btn-primary-erp" id="btn-new-sale">
            <i class="bi bi-cart-plus"></i> New Sale
        </a>
    </div>
</div>

<!-- ============================================================ -->
<!-- STAT CARDS ROW 1                                            -->
<!-- ============================================================ -->
<div class="row g-3 mb-3">
    <div class="col-sm-6 col-xl-4">
        <div class="stat-card">
            <div class="stat-icon blue"><i class="bi bi-box-seam"></i></div>
            <div class="stat-body">
                <div class="stat-value">${stats.totalProducts}</div>
                <div class="stat-label">Total Products</div>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="stretched-link" id="link-products"></a>
        </div>
    </div>
    <div class="col-sm-6 col-xl-4">
        <div class="stat-card">
            <div class="stat-icon green"><i class="bi bi-people"></i></div>
            <div class="stat-body">
                <div class="stat-value">${stats.totalCustomers}</div>
                <div class="stat-label">Total Customers</div>
            </div>
            <a href="${pageContext.request.contextPath}/customers" class="stretched-link" id="link-customers"></a>
        </div>
    </div>
    <div class="col-sm-6 col-xl-4">
        <div class="stat-card">
            <div class="stat-icon orange"><i class="bi bi-receipt"></i></div>
            <div class="stat-body">
                <div class="stat-value">${stats.totalSales}</div>
                <div class="stat-label">Total Sales</div>
            </div>
            <a href="${pageContext.request.contextPath}/sales" class="stretched-link" id="link-sales"></a>
        </div>
    </div>
    <div class="col-sm-6 col-xl-4">
        <div class="stat-card">
            <div class="stat-icon purple"><i class="bi bi-currency-rupee"></i></div>
            <div class="stat-body">
                <div class="stat-value">
                    ₹<fmt:formatNumber value="${stats.totalRevenue}" pattern="#,##,##0.00"/>
                </div>
                <div class="stat-label">Total Revenue</div>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-4">
        <div class="stat-card">
            <div class="stat-icon teal"><i class="bi bi-layers"></i></div>
            <div class="stat-body">
                <div class="stat-value">${stats.totalInventoryQuantity}</div>
                <div class="stat-label">Inventory Units</div>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-4">
        <div class="stat-card">
            <div class="stat-icon red"><i class="bi bi-exclamation-triangle"></i></div>
            <div class="stat-body">
                <div class="stat-value">${stats.lowStockCount}</div>
                <div class="stat-label">Low Stock Products</div>
            </div>
        </div>
    </div>
</div>

<!-- ============================================================ -->
<!-- RECENT SALES + LOW STOCK                                    -->
<!-- ============================================================ -->
<div class="row g-3">

    <!-- Recent Sales Table -->
    <div class="col-lg-8">
        <div class="erp-card h-100">
            <div class="erp-card-header">
                <h5><i class="bi bi-clock-history me-2 text-primary"></i>Recent Sales</h5>
                <a href="${pageContext.request.contextPath}/sales" class="btn-secondary-erp btn-sm-erp" id="link-all-sales">
                    View All <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            <div class="table-responsive">
                <table class="erp-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Customer</th>
                            <th>Amount</th>
                            <th>Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty stats.recentSales}">
                                <tr>
                                    <td colspan="5" class="table-empty">
                                        <i class="bi bi-receipt text-muted"></i>
                                        No sales recorded yet.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="sale" items="${stats.recentSales}">
                                    <tr>
                                        <td><span class="fw-600">#${sale.id}</span></td>
                                        <td>${sale.customerName}</td>
                                        <td class="fw-600 text-primary">
                                            ₹<fmt:formatNumber value="${sale.totalAmount}" pattern="#,##,##0.00"/>
                                        </td>
                                        <td class="text-secondary fs-sm">${sale.saleDate}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/sales/${sale.id}"
                                               class="btn-secondary-erp btn-sm-erp" id="link-sale-${sale.id}">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Low Stock Products -->
    <div class="col-lg-4">
        <div class="erp-card h-100">
            <div class="erp-card-header">
                <h5><i class="bi bi-exclamation-triangle me-2 text-danger"></i>Low Stock Alerts</h5>
            </div>
            <div class="erp-card-body p-0">
                <c:choose>
                    <c:when test="${empty stats.lowStockProducts}">
                        <div class="table-empty">
                            <i class="bi bi-check-circle text-success"></i>
                            All products are well-stocked!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${stats.lowStockProducts}">
                            <div class="d-flex align-items-center justify-content-between px-3 py-2 border-bottom" id="lowstock-${p.id}">
                                <div>
                                    <div class="fw-600 fs-sm">${p.name}</div>
                                    <small class="text-muted">ID: ${p.id}</small>
                                </div>
                                <span class="badge-low-stock">
                                    <i class="bi bi-exclamation-triangle-fill me-1"></i>${p.quantity} left
                                </span>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

</div>

<%@ include file="layout/footer.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Products" scope="request"/>
<c:set var="activeMenu" value="products" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-box-seam me-2 text-primary"></i>Products</h1>
        <p>Manage your product inventory.</p>
    </div>
    <div class="d-flex gap-2 align-items-center">
        <!-- Search -->
        <form method="get" action="${pageContext.request.contextPath}/products" class="search-bar" id="searchForm">
            <input type="text" name="search" id="searchInput" class="form-control"
                   placeholder="Search products..." value="${search}">
            <button type="submit" class="btn-secondary-erp" id="btnSearch">
                <i class="bi bi-search"></i>
            </button>
            <c:if test="${not empty search}">
                <a href="${pageContext.request.contextPath}/products" class="btn-secondary-erp" id="btnClearSearch">
                    <i class="bi bi-x"></i>
                </a>
            </c:if>
        </form>
        <a href="${pageContext.request.contextPath}/products/add" class="btn-primary-erp" id="btn-add-product">
            <i class="bi bi-plus-lg"></i> Add Product
        </a>
    </div>
</div>

<div class="erp-card">
    <div class="erp-card-header">
        <h5>
            <i class="bi bi-list-ul me-2"></i>
            Product List
            <c:if test="${not empty search}">
                <span class="badge bg-primary ms-2" style="font-size:0.75rem">Search: "${search}"</span>
            </c:if>
        </h5>
        <span class="text-muted fs-sm">${products.size()} product(s) found</span>
    </div>
    <div class="table-responsive">
        <table class="erp-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Product Name</th>
                    <th>Description</th>
                    <th class="text-end">Price (₹)</th>
                    <th class="text-center">Stock</th>
                    <th class="text-center">Status</th>
                    <th class="text-center">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr>
                            <td colspan="7" class="table-empty">
                                <i class="bi bi-box-seam"></i>
                                No products found.
                                <a href="${pageContext.request.contextPath}/products/add" class="d-block mt-2">Add your first product</a>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${products}" varStatus="st">
                            <tr id="product-row-${p.id}">
                                <td class="text-muted fs-sm">${st.index + 1}</td>
                                <td><span class="fw-600">${p.name}</span></td>
                                <td class="text-secondary fs-sm">
                                    <c:choose>
                                        <c:when test="${p.description.length() > 60}">
                                            ${p.description.substring(0, 60)}...
                                        </c:when>
                                        <c:otherwise>${p.description}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end fw-600">
                                    <fmt:formatNumber value="${p.price}" pattern="#,##,##0.00"/>
                                </td>
                                <td class="text-center fw-600">${p.quantity}</td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${p.lowStock}">
                                            <span class="badge-low-stock"><i class="bi bi-exclamation-triangle-fill me-1"></i>Low Stock</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-ok"><i class="bi bi-check-circle-fill me-1"></i>In Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/products/edit/${p.id}"
                                       class="btn-secondary-erp btn-sm-erp me-1" id="btn-edit-product-${p.id}">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form method="post" id="deleteForm${p.id}"
                                          action="${pageContext.request.contextPath}/products/delete/${p.id}"
                                          style="display:inline;">
                                        <button type="button"
                                                class="btn-danger-erp btn-sm-erp"
                                                onclick="confirmDelete('deleteForm${p.id}', '${p.name}')"
                                                id="btn-delete-product-${p.id}">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>

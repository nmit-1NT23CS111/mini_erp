<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Customers" scope="request"/>
<c:set var="activeMenu" value="customers" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-people me-2 text-primary"></i>Customers</h1>
        <p>Manage your customer database.</p>
    </div>
    <div class="d-flex gap-2 align-items-center">
        <form method="get" action="${pageContext.request.contextPath}/customers" class="search-bar" id="customerSearchForm">
            <input type="text" name="search" id="customerSearchInput" class="form-control"
                   placeholder="Search customers..." value="${search}">
            <button type="submit" class="btn-secondary-erp" id="btnCustomerSearch"><i class="bi bi-search"></i></button>
            <c:if test="${not empty search}">
                <a href="${pageContext.request.contextPath}/customers" class="btn-secondary-erp" id="btnClearCustomerSearch"><i class="bi bi-x"></i></a>
            </c:if>
        </form>
        <a href="${pageContext.request.contextPath}/customers/add" class="btn-primary-erp" id="btn-add-customer">
            <i class="bi bi-person-plus"></i> Add Customer
        </a>
    </div>
</div>

<div class="erp-card">
    <div class="erp-card-header">
        <h5><i class="bi bi-list-ul me-2"></i>Customer List</h5>
        <span class="text-muted fs-sm">${customers.size()} customer(s) found</span>
    </div>
    <div class="table-responsive">
        <table class="erp-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Address</th>
                    <th class="text-center">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty customers}">
                        <tr>
                            <td colspan="6" class="table-empty">
                                <i class="bi bi-people"></i>
                                No customers found.
                                <a href="${pageContext.request.contextPath}/customers/add" class="d-block mt-2">Add your first customer</a>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="c" items="${customers}" varStatus="st">
                            <tr id="customer-row-${c.id}">
                                <td class="text-muted fs-sm">${st.index + 1}</td>
                                <td><span class="fw-600">${c.name}</span></td>
                                <td class="text-secondary fs-sm">
                                    <c:if test="${not empty c.email}">
                                        <a href="mailto:${c.email}" style="color:inherit">${c.email}</a>
                                    </c:if>
                                    <c:if test="${empty c.email}">—</c:if>
                                </td>
                                <td class="text-secondary fs-sm">
                                    <c:choose><c:when test="${not empty c.phone}">${c.phone}</c:when>
                                    <c:otherwise>—</c:otherwise></c:choose>
                                </td>
                                <td class="text-secondary fs-sm">
                                    <c:choose>
                                        <c:when test="${not empty c.address and c.address.length() > 50}">
                                            ${c.address.substring(0,50)}...
                                        </c:when>
                                        <c:when test="${not empty c.address}">${c.address}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/customers/edit/${c.id}"
                                       class="btn-secondary-erp btn-sm-erp" id="btn-edit-customer-${c.id}">
                                        <i class="bi bi-pencil"></i>
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

<%@ include file="../layout/footer.jsp" %>

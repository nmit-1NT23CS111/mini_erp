<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Sales History" scope="request"/>
<c:set var="activeMenu" value="sales" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-receipt me-2 text-primary"></i>Sales History</h1>
        <p>Track every completed transaction and payment total.</p>
    </div>
    <div>
        <a href="${pageContext.request.contextPath}/sales/create" class="btn-primary-erp" id="btn-new-sale-page">
            <i class="bi bi-cart-plus"></i> New Sale
        </a>
    </div>
</div>

<div class="erp-card">
    <div class="erp-card-header">
        <h5><i class="bi bi-list-ul me-2"></i>Recent Transactions</h5>
        <span class="text-muted fs-sm">${sales.size()} sale(s)</span>
    </div>
    <div class="table-responsive">
        <table class="erp-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Customer</th>
                    <th>Total Amount</th>
                    <th>Date</th>
                    <th class="text-center">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty sales}">
                        <tr>
                            <td colspan="5" class="table-empty">
                                <i class="bi bi-receipt"></i>
                                No sales recorded yet.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="sale" items="${sales}" varStatus="status">
                            <tr>
                                <td class="text-muted fs-sm">#${sale.id}</td>
                                <td class="fw-600">${sale.customerName}</td>
                                <td class="fw-600 text-primary">
                                    ₹<fmt:formatNumber value="${sale.totalAmount}" pattern="#,##,##0.00"/>
                                </td>
                                <td class="text-secondary fs-sm">${sale.saleDate}</td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/sales/${sale.id}"
                                       class="btn-secondary-erp btn-sm-erp" id="btn-sale-detail-${sale.id}">
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

<%@ include file="../layout/footer.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found" scope="request"/>
<c:set var="activeMenu" value="dashboard" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-exclamation-octagon me-2 text-warning"></i>404 - Page Not Found</h1>
        <p>The page you requested does not exist or may have moved.</p>
    </div>
</div>

<div class="erp-card text-center py-5">
    <div class="display-1 fw-bold text-muted">404</div>
    <p class="lead text-secondary">We could not find the resource you were looking for.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn-primary-erp">
        <i class="bi bi-house-door me-1"></i>Go to Dashboard
    </a>
</div>

<%@ include file="../layout/footer.jsp" %>

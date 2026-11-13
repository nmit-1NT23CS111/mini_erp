<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Server Error" scope="request"/>
<c:set var="activeMenu" value="dashboard" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-bug me-2 text-danger"></i>500 - Server Error</h1>
        <p>Something went wrong while processing your request.</p>
    </div>
</div>

<div class="erp-card text-center py-5">
    <div class="display-1 fw-bold text-danger">500</div>
    <p class="lead text-secondary">Please try again or contact the application administrator.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn-primary-erp">
        <i class="bi bi-arrow-left-circle me-1"></i>Back to Dashboard
    </a>
</div>

<%@ include file="../layout/footer.jsp" %>

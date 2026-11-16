<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Shared HTML <head> + sidebar layout opener. Include at top of every page. --%>
<%-- Requires: pageTitle, activeMenu --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} – Mini ERP</title>
    <meta name="description" content="Mini ERP – ${pageTitle}">
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
</head>
<body>

<!-- ================================================================== -->
<!-- SIDEBAR                                                             -->
<!-- ================================================================== -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-brand">
        <div class="brand-icon"><i class="bi bi-graph-up-arrow"></i></div>
        <div>
            <div class="brand-name">Mini ERP</div>
            <div class="brand-sub">Management System</div>
        </div>
    </div>

    <nav class="sidebar-nav">
        <div class="nav-label">Main Menu</div>
        <a href="${pageContext.request.contextPath}/dashboard"
           class="nav-item ${activeMenu == 'dashboard' ? 'active' : ''}" id="nav-dashboard">
            <i class="bi bi-speedometer2"></i>
            <span>Dashboard</span>
        </a>
        <a href="${pageContext.request.contextPath}/products"
           class="nav-item ${activeMenu == 'products' ? 'active' : ''}" id="nav-products">
            <i class="bi bi-box-seam"></i>
            <span>Products</span>
        </a>
        <a href="${pageContext.request.contextPath}/customers"
           class="nav-item ${activeMenu == 'customers' ? 'active' : ''}" id="nav-customers">
            <i class="bi bi-people"></i>
            <span>Customers</span>
        </a>
        <a href="${pageContext.request.contextPath}/assistant"
           class="nav-item ${activeMenu == 'assistant' ? 'active' : ''}" id="nav-assistant">
            <i class="bi bi-robot"></i>
            <span>AI Assistant</span>
        </a>

        <div class="nav-label mt-2">Sales</div>
        <a href="${pageContext.request.contextPath}/sales/create"
           class="nav-item ${activeMenu == 'create-sale' ? 'active' : ''}" id="nav-create-sale">
            <i class="bi bi-cart-plus"></i>
            <span>New Sale</span>
        </a>
        <a href="${pageContext.request.contextPath}/sales"
           class="nav-item ${activeMenu == 'sales' ? 'active' : ''}" id="nav-sales">
            <i class="bi bi-receipt"></i>
            <span>Sales History</span>
        </a>
    </nav>

    <div class="sidebar-footer">
        <div class="user-info">
            <div class="user-avatar"><i class="bi bi-person-fill"></i></div>
            <div>
                <div class="user-name">${sessionScope.loggedInUser}</div>
                <div class="user-role">${sessionScope.userRole}</div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout" id="btn-logout">
            <i class="bi bi-box-arrow-left"></i>
        </a>
    </div>
</div>

<!-- ================================================================== -->
<!-- MAIN CONTENT WRAPPER                                               -->
<!-- ================================================================== -->
<div class="main-content" id="mainContent">

    <!-- Top Bar -->
    <div class="topbar">
        <button class="btn-sidebar-toggle" id="sidebarToggle" onclick="toggleSidebar()">
            <i class="bi bi-list"></i>
        </button>
        <div class="topbar-title">${pageTitle}</div>
        <div class="topbar-right">
            <span class="topbar-time" id="topbarTime"></span>
            <a href="${pageContext.request.contextPath}/logout" class="topbar-logout">
                <i class="bi bi-box-arrow-left me-1"></i>Logout
            </a>
        </div>
    </div>

    <!-- Page content starts here -->
    <div class="page-body">

        <!-- Flash Messages -->
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show erp-alert" role="alert" id="alertSuccess">
                <i class="bi bi-check-circle-fill me-2"></i>${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show erp-alert" role="alert" id="alertError">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

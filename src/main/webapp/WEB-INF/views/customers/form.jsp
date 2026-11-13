<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="${formTitle}" scope="request"/>
<c:set var="activeMenu" value="customers" scope="request"/>
<%@ include file="../layout/header.jsp"%>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-people me-2 text-primary"></i>${formTitle}</h1>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/customers">Customers</a></li>
                <li class="breadcrumb-item active">${formTitle}</li>
            </ol>
        </nav>
    </div>
</div>

<div class="row justify-content-center">
    <div class="col-lg-7">
        <div class="erp-card">
            <div class="erp-card-header">
                <h5><i class="bi bi-person-lines-fill me-2"></i>${formTitle}</h5>
            </div>
            <div class="erp-card-body">
                <form method="post" action="${pageContext.request.contextPath}${action}" id="customerForm">
                    <div class="mb-3">
                        <label for="name" class="form-label">Customer Name <span class="text-danger">*</span></label>
                        <input type="text" id="name" name="name" class="form-control"
                               value="${customer.name}" maxlength="150" required>
                    </div>
                    <div class="row g-3 mb-3">
                        <div class="col-sm-6">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" id="email" name="email" class="form-control"
                                   value="${customer.email}" maxlength="200">
                        </div>
                        <div class="col-sm-6">
                            <label for="phone" class="form-label">Phone</label>
                            <input type="tel" id="phone" name="phone" class="form-control"
                                   value="${customer.phone}" pattern="[+]?[0-9]{7,15}" maxlength="20">
                            <div class="form-text">Use 7 to 15 digits, optionally prefixed with +.</div>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <textarea id="address" name="address" class="form-control" rows="3">${customer.address}</textarea>
                    </div>
                    <hr class="my-3">
                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-primary-erp" id="btnSubmitCustomer">
                            <i class="bi bi-check-lg me-1"></i>Save Customer
                        </button>
                        <a href="${pageContext.request.contextPath}/customers" class="btn-secondary-erp" id="btnCancelCustomer">
                            <i class="bi bi-x me-1"></i>Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp"%>

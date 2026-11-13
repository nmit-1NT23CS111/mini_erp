<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="${formTitle}" scope="request"/>
<c:set var="activeMenu" value="products" scope="request"/>
<%@ include file="../layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-box-seam me-2 text-primary"></i>${formTitle}</h1>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
                <li class="breadcrumb-item active">${formTitle}</li>
            </ol>
        </nav>
    </div>
</div>

<div class="row justify-content-center">
    <div class="col-lg-7">
        <div class="erp-card">
            <div class="erp-card-header">
                <h5><i class="bi bi-pencil-square me-2"></i>${formTitle}</h5>
            </div>
            <div class="erp-card-body">
                <form method="post" action="${pageContext.request.contextPath}${action}" id="productForm">

                    <div class="mb-3">
                        <label for="name" class="form-label">Product Name <span class="text-danger">*</span></label>
                        <input type="text" id="name" name="name" class="form-control"
                               value="${product.name}" placeholder="e.g. Laptop Pro 15" required maxlength="150">
                        <div class="form-text">Required. Maximum 150 characters.</div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea id="description" name="description" class="form-control"
                                  rows="3" placeholder="Brief product description..."
                                  maxlength="1000">${product.description}</textarea>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-sm-6">
                            <label for="price" class="form-label">Price (₹) <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text">₹</span>
                                <input type="number" id="price" name="price" class="form-control"
                                       value="${product.price}" placeholder="0.00"
                                       min="0.01" step="0.01" required>
                            </div>
                            <div class="form-text">Must be greater than zero.</div>
                        </div>
                        <div class="col-sm-6">
                            <label for="quantity" class="form-label">Stock Quantity <span class="text-danger">*</span></label>
                            <input type="number" id="quantity" name="quantity" class="form-control"
                                   value="${product.quantity}" placeholder="0"
                                   min="0" required>
                            <div class="form-text">Cannot be negative.</div>
                        </div>
                    </div>

                    <hr class="my-3">

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-primary-erp" id="btnSubmitProduct">
                            <i class="bi bi-check-lg me-1"></i>Save Product
                        </button>
                        <a href="${pageContext.request.contextPath}/products" class="btn-secondary-erp" id="btnCancelProduct">
                            <i class="bi bi-x me-1"></i>Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>

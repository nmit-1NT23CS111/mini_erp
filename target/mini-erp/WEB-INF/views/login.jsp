<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mini ERP – Login</title>
    <meta name="description" content="Mini ERP Sales and Inventory Management System – Admin Login">
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f2027 0%, #203a43 50%, #2c5364 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1rem;
        }
        .login-wrapper {
            width: 100%;
            max-width: 420px;
        }
        .login-logo {
            text-align: center;
            margin-bottom: 2rem;
        }
        .login-logo .logo-icon {
            width: 70px; height: 70px;
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            border-radius: 20px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1rem;
            box-shadow: 0 10px 30px rgba(37,99,235,0.4);
        }
        .login-logo .logo-icon i { font-size: 2rem; color: #fff; }
        .login-logo h1 { font-size: 1.6rem; font-weight: 700; color: #fff; margin: 0; }
        .login-logo p { font-size: 0.85rem; color: rgba(255,255,255,0.6); margin: 0.3rem 0 0; }
        .login-card {
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 20px;
            padding: 2.5rem;
        }
        .login-card h2 {
            color: #fff;
            font-size: 1.3rem;
            font-weight: 600;
            margin-bottom: 1.75rem;
            text-align: center;
        }
        .form-label { color: rgba(255,255,255,0.75); font-size: 0.85rem; font-weight: 500; }
        .form-control {
            background: rgba(255,255,255,0.08);
            border: 1px solid rgba(255,255,255,0.15);
            color: #fff;
            border-radius: 10px;
            padding: 0.75rem 1rem;
            font-size: 0.95rem;
        }
        .form-control::placeholder { color: rgba(255,255,255,0.3); }
        .form-control:focus {
            background: rgba(255,255,255,0.12);
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37,99,235,0.3);
            color: #fff;
        }
        .input-group-text {
            background: rgba(255,255,255,0.08);
            border: 1px solid rgba(255,255,255,0.15);
            color: rgba(255,255,255,0.5);
            border-radius: 10px 0 0 10px;
        }
        .input-group .form-control { border-radius: 0 10px 10px 0; }
        .btn-login {
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            border: none;
            border-radius: 10px;
            padding: 0.8rem;
            font-size: 1rem;
            font-weight: 600;
            color: #fff;
            width: 100%;
            transition: all 0.2s;
            box-shadow: 0 4px 15px rgba(37,99,235,0.4);
        }
        .btn-login:hover {
            transform: translateY(-1px);
            box-shadow: 0 6px 20px rgba(37,99,235,0.5);
            color: #fff;
        }
        .alert-danger-custom {
            background: rgba(220,53,69,0.15);
            border: 1px solid rgba(220,53,69,0.3);
            color: #f87171;
            border-radius: 10px;
            padding: 0.75rem 1rem;
            margin-bottom: 1.25rem;
            font-size: 0.9rem;
        }
        .login-footer { text-align: center; margin-top: 1.5rem; }
        .login-footer p { color: rgba(255,255,255,0.4); font-size: 0.8rem; }
        .demo-hint {
            background: rgba(37,99,235,0.1);
            border: 1px solid rgba(37,99,235,0.3);
            border-radius: 10px;
            padding: 0.65rem 1rem;
            color: rgba(255,255,255,0.6);
            font-size: 0.8rem;
            margin-top: 1rem;
            text-align: center;
        }
        .demo-hint strong { color: #93c5fd; }
    </style>
</head>
<body>
<div class="login-wrapper">
    <div class="login-logo">
        <div class="logo-icon"><i class="bi bi-graph-up-arrow"></i></div>
        <h1>Mini ERP</h1>
        <p>Sales &amp; Inventory Management System</p>
    </div>

    <div class="login-card">
        <h2>Welcome Back</h2>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert-danger-custom">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert-danger-custom" style="color:#86efac;border-color:rgba(34,197,94,0.3);background:rgba(34,197,94,0.1);">
                <i class="bi bi-check-circle-fill me-2"></i>${success}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-person"></i></span>
                    <input type="text" id="username" name="username" class="form-control"
                           placeholder="Enter username" required autocomplete="username">
                </div>
            </div>
            <div class="mb-4">
                <label for="password" class="form-label">Password</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-lock"></i></span>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Enter password" required autocomplete="current-password">
                </div>
            </div>
            <button type="submit" class="btn btn-login" id="btnLogin">
                <i class="bi bi-box-arrow-in-right me-2"></i>Sign In
            </button>
        </form>

        <div class="demo-hint">
            <i class="bi bi-info-circle me-1"></i>
            Demo credentials: <strong>admin</strong> / <strong>Admin@123</strong>
        </div>
    </div>

    <div class="login-footer">
        <p>&copy; 2024 Mini ERP System &nbsp;|&nbsp; College Project</p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

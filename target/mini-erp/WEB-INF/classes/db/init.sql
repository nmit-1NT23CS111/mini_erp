-- ============================================================
-- Mini ERP – Database Initialization Script
-- Database: mini_erp
-- Run this script ONCE before starting the application.
-- ============================================================

CREATE DATABASE IF NOT EXISTS mini_erp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mini_erp;

-- ============================================================
-- TABLE: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,          -- BCrypt hash
    role        VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: products
-- ============================================================
CREATE TABLE IF NOT EXISTS products (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150)    NOT NULL,
    description TEXT,
    price       DECIMAL(15,2)   NOT NULL CHECK (price > 0),
    quantity    INT             NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_product_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: customers
-- ============================================================
CREATE TABLE IF NOT EXISTS customers (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    email       VARCHAR(200) UNIQUE,
    phone       VARCHAR(20),
    address     TEXT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_customer_name (name),
    INDEX idx_customer_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: sales
-- ============================================================
CREATE TABLE IF NOT EXISTS sales (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    customer_id     BIGINT          NOT NULL,
    total_amount    DECIMAL(15,2)   NOT NULL,
    sale_date       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
    INDEX idx_sale_date (sale_date),
    INDEX idx_sale_customer (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE: sale_items
-- ============================================================
CREATE TABLE IF NOT EXISTS sale_items (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    sale_id     BIGINT          NOT NULL,
    product_id  BIGINT          NOT NULL,
    quantity    INT             NOT NULL CHECK (quantity > 0),
    unit_price  DECIMAL(15,2)   NOT NULL,       -- price at time of sale
    subtotal    DECIMAL(15,2)   NOT NULL,
    FOREIGN KEY (sale_id)    REFERENCES sales(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    INDEX idx_sale_items_sale (sale_id),
    INDEX idx_sale_items_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- DEMO DATA
-- ============================================================

-- Admin user: username=admin  password=Admin@123
-- BCrypt hash for "Admin@123" (strength 10)
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$kQBknTRDhY7d3XdxND5DM.P894hSEm3wlgq0dWnvXbYA9d.FTU5Cq', 'ADMIN')
ON DUPLICATE KEY UPDATE password = VALUES(password), role = VALUES(role);

-- Demo products
INSERT INTO products (name, description, price, quantity)
SELECT demo.name, demo.description, demo.price, demo.quantity
FROM (
    SELECT 'Laptop' AS name, 'High-performance laptop with 16GB RAM, 512GB SSD' AS description, 50000.00 AS price, 10 AS quantity
    UNION ALL SELECT 'Mouse', 'Wireless optical mouse with USB receiver', 500.00, 50
    UNION ALL SELECT 'Keyboard', 'Mechanical keyboard with RGB backlight', 3000.00, 30
    UNION ALL SELECT 'Monitor', '27-inch Full HD LED monitor', 18000.00, 8
    UNION ALL SELECT 'Headphones', 'Noise-cancelling over-ear headphones', 4500.00, 20
    UNION ALL SELECT 'Webcam', '1080p HD webcam with built-in microphone', 2200.00, 15
    UNION ALL SELECT 'USB Hub', '7-port USB 3.0 hub with power adapter', 800.00, 40
    UNION ALL SELECT 'Printer', 'Wireless inkjet colour printer', 8500.00, 5
) AS demo
WHERE NOT EXISTS (SELECT 1 FROM products p WHERE p.name = demo.name);

-- Demo customers
INSERT INTO customers (name, email, phone, address)
SELECT demo.name, demo.email, demo.phone, demo.address
FROM (
    SELECT 'Rahul Sharma' AS name, 'rahul.sharma@example.com' AS email, '9876543210' AS phone, '12 MG Road, Bangalore, Karnataka 560001' AS address
    UNION ALL SELECT 'Priya Patel', 'priya.patel@example.com', '9988776655', '45 Park Street, Mumbai, Maharashtra 400001'
    UNION ALL SELECT 'Amit Kumar', 'amit.kumar@example.com', '9123456789', '78 Civil Lines, New Delhi 110001'
    UNION ALL SELECT 'Sunita Singh', 'sunita.singh@example.com', '9765432100', '23 Anna Nagar, Chennai, Tamil Nadu 600040'
    UNION ALL SELECT 'Vikram Reddy', 'vikram.reddy@example.com', '9654321098', '56 Jubilee Hills, Hyderabad, Telangana 500033'
) AS demo
WHERE NOT EXISTS (SELECT 1 FROM customers c WHERE c.email = demo.email);

-- ============================================================
-- Verification query – run after import to confirm data
-- ============================================================
-- SELECT 'users' AS tbl, COUNT(*) AS rows FROM users
-- UNION ALL SELECT 'products', COUNT(*) FROM products
-- UNION ALL SELECT 'customers', COUNT(*) FROM customers
-- UNION ALL SELECT 'sales', COUNT(*) FROM sales
-- UNION ALL SELECT 'sale_items', COUNT(*) FROM sale_items;

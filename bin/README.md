# Mini ERP – Sales and Inventory Management System

A Spring MVC + JDBC + JSP application for managing products, customers, sales, and dashboard analytics in a college demo environment.

## Tech Stack

- Java 21+
- Spring Framework 5.3.x
- Spring MVC
- Spring JDBC + JdbcTemplate
- Spring Transaction Management
- MySQL 8
- JSP / JSTL
- Bootstrap 5
- Maven
- Apache Tomcat 9

## Prerequisites

1. JDK 21 or later installed.
2. Maven installed and available on PATH.
3. MySQL 8 installed and running.
4. Tomcat 9 installed for deployment.

## MySQL Setup

1. Create a database named `mini_erp`.
2. Use the schema in `src/main/resources/db/init.sql`.
3. Update credentials in `src/main/resources/jdbc.properties`.

Example configuration:

```properties
jdbc.url=jdbc:mysql://localhost:3306/mini_erp?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
jdbc.username=root
jdbc.password=your_mysql_password
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```

## Database Initialization

Run the SQL script against MySQL:

```bash
mysql -u root -p < src/main/resources/db/init.sql
```

This script creates:

- `users`
- `products`
- `customers`
- `sales`
- `sale_items`

It also inserts a default admin user:

- Username: `admin`
- Password: `Admin@123`

## Build

From the project root:

```bash
mvn clean package
```

This produces:

- `target/mini-erp.war`

## Tomcat Deployment

1. Copy the WAR file to Tomcat's `webapps` directory.
2. Start Tomcat.
3. Open:

```text
http://localhost:8080/mini-erp/
```

If you deploy the app under a different context path, update the corresponding URL.

## Default Login

- URL: `/login`
- Username: `admin`
- Password: `Admin@123`

## REST API

Base URL:

```text
http://localhost:8080/mini-erp/api
```

### Authentication

- `POST /api/login`
- `POST /api/logout`

### Products

- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

### Customers

- `GET /api/customers`
- `POST /api/customers`
- `PUT /api/customers/{id}`

### Sales

- `GET /api/sales`
- `GET /api/sales/{id}`
- `POST /api/sales`

### Dashboard

- `GET /api/dashboard`

## Demo Scenario

1. Log in as `admin`.
2. Verify the dashboard loads.
3. Add a product like Laptop - Price ₹50,000 - Stock 10.
4. Add a customer Rahul.
5. Create a sale for Rahul with Laptop quantity 2.
6. Confirm total is ₹1,00,000 and inventory becomes 8.
7. Check sales history and dashboard revenue update.
8. Try selling 20 laptops to confirm insufficient stock is rejected.

## Troubleshooting

- If the app cannot connect to MySQL, check `jdbc.properties` and confirm the MySQL service is running.
- If a page is missing, verify the JSP file exists under `src/main/webapp/WEB-INF/views`.
- If Maven cannot find Java, set `JAVA_HOME` to a valid JDK 21+ installation.
- If Tomcat fails to deploy, ensure the WAR file is copied to `webapps` and the service has permission to read the app.

## Notes

This project is designed as a guided business-application demo. It preserves a plain Spring MVC architecture and avoids Spring Boot, JPA, or microservices.

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

## AI Business Assistant

The project now includes an AI Business Assistant powered by a locally installed Ollama model.

### Setup

1. Make sure Ollama is installed and running locally on:
   `http://localhost:11434`
2. Confirm the model exists:
   `llama3.2:3b`
3. Start the Ollama service if needed:

```bash
ollama serve
ollama pull llama3.2:3b
```

4. The assistant configuration is stored in:
   `src/main/resources/ollama.properties`

Example:

```properties
ollama.base-url=http://localhost:11434
ollama.model=llama3.2:3b
ollama.connect-timeout-ms=10000
ollama.timeout-ms=600000
```

The connect timeout defaults to 10 seconds; the generation timeout defaults to 10 minutes to allow slower local models to finish.

### Access the assistant

- Page URL: `/assistant`
- Sidebar menu item: `AI Assistant`

The assistant can answer questions such as:

- How many products do we have?
- Which products have low stock?
- What is the total sales revenue?
- Which are the top five selling products?
- Who are our top customers?
- Show recent sales.
- Summarize current business performance.

The assistant uses the ERP’s own database data and passes only read-only business summaries to Ollama. It does not execute unsafe SQL or modify data.

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

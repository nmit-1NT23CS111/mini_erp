package com.minierp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a product in the inventory.
 */
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private LocalDateTime createdAt;

    // Low stock threshold
    public static final int LOW_STOCK_THRESHOLD = 5;

    public Product() {}

    public Product(Long id, String name, String description,
                   BigDecimal price, int quantity, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    /** Returns true when stock is at or below the low-stock threshold. */
    public boolean isLowStock() {
        return this.quantity <= LOW_STOCK_THRESHOLD;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

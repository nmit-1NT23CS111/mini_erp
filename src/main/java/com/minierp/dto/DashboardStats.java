package com.minierp.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dashboard statistics transferred to the view / REST client.
 */
public class DashboardStats {
    private long totalProducts;
    private long totalCustomers;
    private long totalSales;
    private BigDecimal totalRevenue;
    private long totalInventoryQuantity;
    private long lowStockCount;
    private List<RecentSale> recentSales;
    private List<LowStockProduct> lowStockProducts;

    // Getters & Setters
    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }

    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }

    public long getTotalSales() { return totalSales; }
    public void setTotalSales(long totalSales) { this.totalSales = totalSales; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public long getTotalInventoryQuantity() { return totalInventoryQuantity; }
    public void setTotalInventoryQuantity(long totalInventoryQuantity) { this.totalInventoryQuantity = totalInventoryQuantity; }

    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }

    public List<RecentSale> getRecentSales() { return recentSales; }
    public void setRecentSales(List<RecentSale> recentSales) { this.recentSales = recentSales; }

    public List<LowStockProduct> getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(List<LowStockProduct> lowStockProducts) { this.lowStockProducts = lowStockProducts; }

    // ------------------------------------------------------------------ //
    // Nested DTOs                                                         //
    // ------------------------------------------------------------------ //
    public static class RecentSale {
        private Long id;
        private String customerName;
        private BigDecimal totalAmount;
        private String saleDate;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public String getSaleDate() { return saleDate; }
        public void setSaleDate(String saleDate) { this.saleDate = saleDate; }
    }

    public static class LowStockProduct {
        private Long id;
        private String name;
        private int quantity;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}

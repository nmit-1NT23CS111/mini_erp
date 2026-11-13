package com.minierp.service;

import com.minierp.dao.CustomerDao;
import com.minierp.dao.ProductDao;
import com.minierp.dao.SaleDao;
import com.minierp.dto.DashboardStats;
import com.minierp.model.Product;
import com.minierp.model.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired private ProductDao productDao;
    @Autowired private CustomerDao customerDao;
    @Autowired private SaleDao saleDao;

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        // Core counters
        stats.setTotalProducts(productDao.count());
        stats.setTotalCustomers(customerDao.count());
        stats.setTotalSales(saleDao.count());
        stats.setTotalRevenue(saleDao.totalRevenue());
        stats.setTotalInventoryQuantity(productDao.totalInventoryQuantity());

        // Low stock
        List<Product> lowStockProducts = productDao.findLowStock(Product.LOW_STOCK_THRESHOLD);
        stats.setLowStockCount(lowStockProducts.size());
        stats.setLowStockProducts(lowStockProducts.stream().map(p -> {
            DashboardStats.LowStockProduct ls = new DashboardStats.LowStockProduct();
            ls.setId(p.getId());
            ls.setName(p.getName());
            ls.setQuantity(p.getQuantity());
            return ls;
        }).collect(Collectors.toList()));

        // Recent sales (last 10)
        List<Sale> recentSales = saleDao.findRecent(10);
        stats.setRecentSales(recentSales.stream().map(s -> {
            DashboardStats.RecentSale rs = new DashboardStats.RecentSale();
            rs.setId(s.getId());
            rs.setCustomerName(s.getCustomerName());
            rs.setTotalAmount(s.getTotalAmount());
            rs.setSaleDate(s.getSaleDate().format(FORMATTER));
            return rs;
        }).collect(Collectors.toList()));

        return stats;
    }
}

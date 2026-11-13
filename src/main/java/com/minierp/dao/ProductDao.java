package com.minierp.dao;

import com.minierp.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDao {
    List<Product> findAll();
    List<Product> search(String keyword);
    Optional<Product> findById(Long id);
    Long save(Product product);
    int update(Product product);
    int delete(Long id);
    boolean hasSalesHistory(Long id);
    int decreaseQuantity(Long id, int quantity);
    List<Product> findLowStock(int threshold);
    long count();
    long totalInventoryQuantity();
}

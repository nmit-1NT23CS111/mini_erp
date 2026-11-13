package com.minierp.service;

import com.minierp.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> searchProducts(String keyword);
    Product getProductById(Long id);
    Long addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Long id);
    List<Product> getLowStockProducts();
}

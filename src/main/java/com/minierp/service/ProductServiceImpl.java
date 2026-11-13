package com.minierp.service;

import com.minierp.dao.ProductDao;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return productDao.findAll();
        }
        return productDao.search(keyword.trim());
    }

    @Override
    public Product getProductById(Long id) {
        return productDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public Long addProduct(Product product) {
        validate(product);
        return productDao.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        validate(product);
        // Ensure product exists
        productDao.findById(product.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Product", product.getId()));
        productDao.update(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        if (productDao.hasSalesHistory(id)) {
            throw new IllegalStateException(
                "Cannot delete product: it has associated sales history.");
        }
        productDao.delete(id);
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productDao.findLowStock(Product.LOW_STOCK_THRESHOLD);
    }

    // ------------------------------------------------------------------ //
    // Validation                                                          //
    // ------------------------------------------------------------------ //
    private void validate(Product p) {
        if (p.getName() == null || p.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (p.getPrice() == null || p.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (p.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
    }
}

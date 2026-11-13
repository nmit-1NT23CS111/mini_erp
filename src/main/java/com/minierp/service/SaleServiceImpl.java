package com.minierp.service;

import com.minierp.dao.CustomerDao;
import com.minierp.dao.ProductDao;
import com.minierp.dao.SaleDao;
import com.minierp.dto.SaleRequest;
import com.minierp.exception.InsufficientStockException;
import com.minierp.exception.ResourceNotFoundException;
import com.minierp.model.Product;
import com.minierp.model.Sale;
import com.minierp.model.SaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CustomerDao customerDao;

    // ------------------------------------------------------------------ //
    // Create Sale – ATOMIC transaction                                    //
    // ------------------------------------------------------------------ //
    @Override
    @Transactional          // rolls back everything on any exception
    public Sale createSale(SaleRequest request) {
        // 1. Validate customer exists
        customerDao.findById(request.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));

        // 2. Validate items list is non-empty
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("A sale must have at least one product.");
        }

        List<SaleItem> saleItems = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;

        // 3. Validate each item and build SaleItem objects
        for (SaleRequest.SaleItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }

            Product product = productDao.findById(itemReq.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", itemReq.getProductId()));

            // Stock check
            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new InsufficientStockException(
                    product.getName(), product.getQuantity(), itemReq.getQuantity());
            }

            BigDecimal unitPrice = product.getPrice();                              // server-side price
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            grandTotal = grandTotal.add(subtotal);

            SaleItem si = new SaleItem();
            si.setProductId(itemReq.getProductId());
            si.setQuantity(itemReq.getQuantity());
            si.setUnitPrice(unitPrice);
            si.setSubtotal(subtotal);
            saleItems.add(si);

            // Keep a reference to the product for the stock update below
            si.setProductName(product.getName());
        }

        // 4. Insert the sale header
        Sale sale = new Sale();
        sale.setCustomerId(request.getCustomerId());
        sale.setTotalAmount(grandTotal);
        Long saleId = saleDao.saveSale(sale);
        sale.setId(saleId);

        // 5. Insert each sale item and reduce stock
        for (int i = 0; i < saleItems.size(); i++) {
            SaleItem si = saleItems.get(i);
            si.setSaleId(saleId);
            saleDao.saveSaleItem(si);

            SaleRequest.SaleItemRequest req = request.getItems().get(i);
            if (productDao.decreaseQuantity(req.getProductId(), req.getQuantity()) == 0) {
                Product product = productDao.findById(req.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", req.getProductId()));
                throw new InsufficientStockException(
                    product.getName(), product.getQuantity(), req.getQuantity());
            }
        }

        sale.setItems(saleItems);
        return sale;
    }

    @Override
    public List<Sale> getAllSales() {
        return saleDao.findAll();
    }

    @Override
    public Sale getSaleById(Long id) {
        Sale sale = saleDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
        sale.setItems(saleDao.findItemsBySaleId(id));
        return sale;
    }
}

package com.minierp.service;

import com.minierp.dto.SaleRequest;
import com.minierp.model.Sale;
import java.util.List;

public interface SaleService {
    /**
     * Creates a complete sale: validates stock, inserts sale + items, updates inventory.
     * All in one database transaction.
     */
    Sale createSale(SaleRequest request);

    List<Sale> getAllSales();
    Sale getSaleById(Long id);
}

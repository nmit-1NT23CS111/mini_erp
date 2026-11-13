package com.minierp.dao;

import com.minierp.model.Sale;
import com.minierp.model.SaleItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SaleDao {
    Long saveSale(Sale sale);
    void saveSaleItem(SaleItem item);
    List<Sale> findAll();
    Optional<Sale> findById(Long id);
    List<SaleItem> findItemsBySaleId(Long saleId);
    long count();
    BigDecimal totalRevenue();
    List<Sale> findRecent(int limit);
}

package com.minierp.dao;

import com.minierp.model.Sale;
import com.minierp.model.SaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class SaleDaoImpl implements SaleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Sale> SALE_ROW_MAPPER = (rs, rowNum) -> {
        Sale s = new Sale();
        s.setId(rs.getLong("id"));
        s.setCustomerId(rs.getLong("customer_id"));
        s.setCustomerName(rs.getString("customer_name"));
        s.setTotalAmount(rs.getBigDecimal("total_amount"));
        s.setSaleDate(rs.getTimestamp("sale_date").toLocalDateTime());
        return s;
    };

    private static final RowMapper<SaleItem> ITEM_ROW_MAPPER = (rs, rowNum) -> {
        SaleItem si = new SaleItem();
        si.setId(rs.getLong("id"));
        si.setSaleId(rs.getLong("sale_id"));
        si.setProductId(rs.getLong("product_id"));
        si.setProductName(rs.getString("product_name"));
        si.setQuantity(rs.getInt("quantity"));
        si.setUnitPrice(rs.getBigDecimal("unit_price"));
        si.setSubtotal(rs.getBigDecimal("subtotal"));
        return si;
    };

    @Override
    public Long saveSale(Sale sale) {
        String sql = "INSERT INTO sales (customer_id, total_amount) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, sale.getCustomerId());
            ps.setBigDecimal(2, sale.getTotalAmount());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveSaleItem(SaleItem item) {
        String sql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            item.getSaleId(), item.getProductId(),
            item.getQuantity(), item.getUnitPrice(), item.getSubtotal());
    }

    @Override
    public List<Sale> findAll() {
        String sql = "SELECT s.id, s.customer_id, c.name AS customer_name, " +
                     "s.total_amount, s.sale_date " +
                     "FROM sales s " +
                     "JOIN customers c ON s.customer_id = c.id " +
                     "ORDER BY s.sale_date DESC";
        return jdbcTemplate.query(sql, SALE_ROW_MAPPER);
    }

    @Override
    public Optional<Sale> findById(Long id) {
        String sql = "SELECT s.id, s.customer_id, c.name AS customer_name, " +
                     "s.total_amount, s.sale_date " +
                     "FROM sales s " +
                     "JOIN customers c ON s.customer_id = c.id " +
                     "WHERE s.id = ?";
        List<Sale> list = jdbcTemplate.query(sql, SALE_ROW_MAPPER, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<SaleItem> findItemsBySaleId(Long saleId) {
        String sql = "SELECT si.id, si.sale_id, si.product_id, p.name AS product_name, " +
                     "si.quantity, si.unit_price, si.subtotal " +
                     "FROM sale_items si " +
                     "JOIN products p ON si.product_id = p.id " +
                     "WHERE si.sale_id = ?";
        return jdbcTemplate.query(sql, ITEM_ROW_MAPPER, saleId);
    }

    @Override
    public long count() {
        Long c = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sales", Long.class);
        return c == null ? 0 : c;
    }

    @Override
    public BigDecimal totalRevenue() {
        BigDecimal rev = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(total_amount), 0) FROM sales", BigDecimal.class);
        return rev == null ? BigDecimal.ZERO : rev;
    }

    @Override
    public List<Sale> findRecent(int limit) {
        String sql = "SELECT s.id, s.customer_id, c.name AS customer_name, " +
                     "s.total_amount, s.sale_date " +
                     "FROM sales s " +
                     "JOIN customers c ON s.customer_id = c.id " +
                     "ORDER BY s.sale_date DESC LIMIT ?";
        return jdbcTemplate.query(sql, SALE_ROW_MAPPER, limit);
    }
}

package com.minierp.dao;

import com.minierp.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Product> ROW_MAPPER = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setQuantity(rs.getInt("quantity"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return p;
    };

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(
            "SELECT id, name, description, price, quantity, created_at FROM products ORDER BY name",
            ROW_MAPPER);
    }

    @Override
    public List<Product> search(String keyword) {
        String sql = "SELECT id, name, description, price, quantity, created_at FROM products " +
                     "WHERE name LIKE ? OR description LIKE ? ORDER BY name";
        String pattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, ROW_MAPPER, pattern, pattern);
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT id, name, description, price, quantity, created_at FROM products WHERE id = ?";
        List<Product> list = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Long save(Product product) {
        String sql = "INSERT INTO products (name, description, price, quantity) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, price=?, quantity=? WHERE id=?";
        return jdbcTemplate.update(sql,
            product.getName(), product.getDescription(),
            product.getPrice(), product.getQuantity(), product.getId());
    }

    @Override
    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM products WHERE id=?", id);
    }

    @Override
    public boolean hasSalesHistory(Long id) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sale_items WHERE product_id=?", Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public int decreaseQuantity(Long id, int quantity) {
        return jdbcTemplate.update(
            "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?",
            quantity, id, quantity);
    }

    @Override
    public List<Product> findLowStock(int threshold) {
        String sql = "SELECT id, name, description, price, quantity, created_at FROM products " +
                     "WHERE quantity <= ? ORDER BY quantity ASC";
        return jdbcTemplate.query(sql, ROW_MAPPER, threshold);
    }

    @Override
    public long count() {
        Long c = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Long.class);
        return c == null ? 0 : c;
    }

    @Override
    public long totalInventoryQuantity() {
        Long c = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(quantity), 0) FROM products", Long.class);
        return c == null ? 0 : c;
    }
}

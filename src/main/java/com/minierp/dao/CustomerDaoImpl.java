package com.minierp.dao;

import com.minierp.model.Customer;
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
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Customer> ROW_MAPPER = (rs, rowNum) -> {
        Customer c = new Customer();
        c.setId(rs.getLong("id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return c;
    };

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(
            "SELECT id, name, email, phone, address, created_at FROM customers ORDER BY name",
            ROW_MAPPER);
    }

    @Override
    public List<Customer> search(String keyword) {
        String pattern = "%" + keyword + "%";
        String sql = "SELECT id, name, email, phone, address, created_at FROM customers " +
                     "WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? ORDER BY name";
        return jdbcTemplate.query(sql, ROW_MAPPER, pattern, pattern, pattern);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        String sql = "SELECT id, name, email, phone, address, created_at FROM customers WHERE id=?";
        List<Customer> list = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Long save(Customer customer) {
        String sql = "INSERT INTO customers (name, email, phone, address) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int update(Customer customer) {
        String sql = "UPDATE customers SET name=?, email=?, phone=?, address=? WHERE id=?";
        return jdbcTemplate.update(sql,
            customer.getName(), customer.getEmail(),
            customer.getPhone(), customer.getAddress(), customer.getId());
    }

    @Override
    public boolean emailExists(String email, Long excludeId) {
        String sql;
        Integer count;
        if (excludeId == null) {
            sql = "SELECT COUNT(*) FROM customers WHERE email = ?";
            count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        } else {
            sql = "SELECT COUNT(*) FROM customers WHERE email = ? AND id != ?";
            count = jdbcTemplate.queryForObject(sql, Integer.class, email, excludeId);
        }
        return count != null && count > 0;
    }

    @Override
    public long count() {
        Long c = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Long.class);
        return c == null ? 0 : c;
    }
}

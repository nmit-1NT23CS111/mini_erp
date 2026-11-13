package com.minierp.dao;

import com.minierp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return u;
    };

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password, role, created_at FROM users WHERE username = ?";
        List<User> results = jdbcTemplate.query(sql, USER_ROW_MAPPER, username);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}

package com.minierp.dao;

import com.minierp.model.User;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
}

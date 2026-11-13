package com.minierp.service;

import com.minierp.model.User;
import java.util.Optional;

public interface UserService {
    /**
     * Validates login credentials.
     * @return the User if credentials match, empty otherwise.
     */
    Optional<User> authenticate(String username, String rawPassword);
}

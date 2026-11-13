package com.minierp.rest;

import com.minierp.dto.ApiResponse;
import com.minierp.model.User;
import com.minierp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/login
     * Body: { "username": "admin", "password": "Admin@123" }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestBody Map<String, String> credentials,
            HttpSession session) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Username and password are required."));
        }

        Optional<User> userOpt = userService.authenticate(username, password);
        if (userOpt.isPresent()) {
            session.setAttribute("loggedInUser", userOpt.get().getUsername());
            return ResponseEntity.ok(ApiResponse.ok("Login successful.", username));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error("Invalid username or password."));
    }

    /**
     * POST /api/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully.", null));
    }
}

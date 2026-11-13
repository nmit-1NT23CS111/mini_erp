package com.minierp.controller;

import com.minierp.model.User;
import com.minierp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // ------------------------------------------------------------------ //
    // GET /login  →  show login form                                      //
    // ------------------------------------------------------------------ //
    @GetMapping("/login")
    public String showLogin(HttpSession session, Model model) {
        // If already logged in, go to dashboard
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    // ------------------------------------------------------------------ //
    // POST /login → authenticate and redirect                             //
    // ------------------------------------------------------------------ //
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes ra) {
        Optional<User> userOpt = userService.authenticate(username, password);
        if (userOpt.isPresent()) {
            session.setAttribute("loggedInUser", userOpt.get().getUsername());
            session.setAttribute("userRole", userOpt.get().getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            return "redirect:/dashboard";
        }
        ra.addFlashAttribute("error", "Invalid username or password. Please try again.");
        return "redirect:/login";
    }

    // ------------------------------------------------------------------ //
    // GET /logout → invalidate session                                    //
    // ------------------------------------------------------------------ //
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ------------------------------------------------------------------ //
    // GET /  → redirect to dashboard or login                             //
    // ------------------------------------------------------------------ //
    @GetMapping("/")
    public String root(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}

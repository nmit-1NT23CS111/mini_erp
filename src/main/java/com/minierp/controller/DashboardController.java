package com.minierp.controller;

import com.minierp.service.DashboardService;
import com.minierp.dto.DashboardStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        DashboardStats stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        return "dashboard";
    }
}

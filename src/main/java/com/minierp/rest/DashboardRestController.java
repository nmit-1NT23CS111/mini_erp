package com.minierp.rest;

import com.minierp.dto.ApiResponse;
import com.minierp.dto.DashboardStats;
import com.minierp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    @Autowired
    private DashboardService dashboardService;

    /** GET /api/dashboard */
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboard() {
        DashboardStats stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }
}

package com.connect.eduHub.controller;

import com.connect.eduHub.model.response.*;
import com.connect.eduHub.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    

    @GetMapping("/{userId}")
    public ResponseEntity<UserDashboardResponse> getUserDashboard(@PathVariable Long userId) {
        UserDashboardResponse dashboardResponse = dashboardService.getUserDashboard(userId);
        return ResponseEntity.ok(dashboardResponse);
    }
    
    
    
}

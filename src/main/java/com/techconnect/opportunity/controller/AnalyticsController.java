package com.techconnect.opportunity.controller;

import com.techconnect.opportunity.dto.AnalyticsStats;
import com.techconnect.opportunity.dto.ApiResponse;
import com.techconnect.opportunity.service.OpportunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final OpportunityService service;

    public AnalyticsController(OpportunityService service) {
        this.service = service;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getStats() {
        AnalyticsStats stats = service.getStats();
        return ResponseEntity.ok(ApiResponse.of(200, "OK", stats));
    }
}

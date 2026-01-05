package com.techconnect.opportunity.controller;

import com.techconnect.opportunity.dto.ApiResponse;
import com.techconnect.opportunity.dto.OpportunityCreateRequest;
import com.techconnect.opportunity.dto.OpportunityResponse;
import com.techconnect.opportunity.service.OpportunityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService service;

    public OpportunityController(OpportunityService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody OpportunityCreateRequest request) {
        OpportunityResponse response = service.create(request);
        return ResponseEntity.status(201).body(ApiResponse.of(201, "Created", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> get(@PathVariable Long id) {
        OpportunityResponse response = service.getById(id);
        return ResponseEntity.ok(ApiResponse.of(200, "OK", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) com.techconnect.opportunity.model.OpportunityType type) {
        List<OpportunityResponse> list;
        if (keyword != null || type != null) {
            list = service.search(keyword, type);
        } else {
            list = service.listAll();
        }
        return ResponseEntity.ok(ApiResponse.of(200, "OK", list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.of(200, "Deleted", null));
    }
}

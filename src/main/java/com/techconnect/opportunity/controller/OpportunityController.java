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
    private final com.techconnect.opportunity.security.JwtUtil jwtUtil;

    public OpportunityController(OpportunityService service, com.techconnect.opportunity.security.JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
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
            @RequestParam(required = false) com.techconnect.opportunity.model.OpportunityType type,
            @RequestParam(required = false) String tag) {
        List<OpportunityResponse> list;
        if (keyword != null || type != null || tag != null) {
            list = service.search(keyword, type, tag);
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

    @PostMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse> addFavorite(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.of(200, "Added to favorites", null));
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse> removeFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(200, "Removed from favorites", null));
    }

    private String getUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return jwtUtil.extractUsername(token.substring(7));
        }
        throw new RuntimeException("Invalid token");
    }

    @PostMapping("/{id}/team-requests")
    public ResponseEntity<ApiResponse> joinTeamLobby(@PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody java.util.Map<String, String> body) {
        String username = getUsernameFromToken(token);
        String message = body.get("message");
        var response = service.joinTeamLobby(id, username, message);
        return ResponseEntity.ok(ApiResponse.of(200, "Joined team lobby", response));
    }

    @DeleteMapping("/{id}/team-requests")
    public ResponseEntity<ApiResponse> leaveTeamLobby(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String username = getUsernameFromToken(token);
        service.leaveTeamLobby(id, username);
        return ResponseEntity.ok(ApiResponse.of(200, "Left team lobby", null));
    }

    @GetMapping("/{id}/team-requests")
    public ResponseEntity<ApiResponse> getTeamRequests(@PathVariable Long id) {
        var list = service.getTeamRequests(id);
        return ResponseEntity.ok(ApiResponse.of(200, "OK", list));
    }

    @PostMapping("/{id}/insights")
    public ResponseEntity<ApiResponse> addInsight(@PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody com.techconnect.opportunity.dto.InsightRequest request) {
        String username = getUsernameFromToken(token);
        var response = service.addInsight(id, username, request);
        return ResponseEntity.ok(ApiResponse.of(200, "Insight added", response));
    }

    @GetMapping("/{id}/insights")
    public ResponseEntity<ApiResponse> getInsights(@PathVariable Long id) {
        var list = service.getInsights(id);
        return ResponseEntity.ok(ApiResponse.of(200, "OK", list));
    }
}

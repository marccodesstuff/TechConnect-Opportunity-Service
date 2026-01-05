package com.techconnect.opportunity.controller;

import com.techconnect.opportunity.dto.ApiResponse;
import com.techconnect.opportunity.model.ForumCategory;
import com.techconnect.opportunity.model.ForumThread;
import com.techconnect.opportunity.model.ForumPost;
import com.techconnect.opportunity.service.ForumService;
import com.techconnect.opportunity.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final ForumService service;
    private final JwtUtil jwtUtil;

    public ForumController(ForumService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    private String getUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return jwtUtil.extractUsername(token.substring(7));
        }
        throw new RuntimeException("Invalid token");
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getCategories() {
        return ResponseEntity.ok(ApiResponse.of(200, "OK", service.getCategories()));
    }

    @GetMapping("/categories/{id}/threads")
    public ResponseEntity<ApiResponse> getThreads(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(200, "OK", service.getThreadsByCategory(id)));
    }

    @PostMapping("/categories/{id}/threads")
    public ResponseEntity<ApiResponse> createThread(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {
        String username = getUsernameFromToken(token);
        ForumThread thread = service.createThread(id, username, body.get("title"), body.get("content"));
        return ResponseEntity.status(201).body(ApiResponse.of(201, "Created", thread));
    }

    @GetMapping("/threads/{id}")
    public ResponseEntity<ApiResponse> getThread(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(200, "OK", service.getThread(id)));
    }

    @PostMapping("/threads/{id}/posts")
    public ResponseEntity<ApiResponse> createPost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {
        String username = getUsernameFromToken(token);
        ForumPost post = service.createPost(id, username, body.get("content"));
        return ResponseEntity.status(201).body(ApiResponse.of(201, "Created", post));
    }
}

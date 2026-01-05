package com.techconnect.opportunity.controller;

import com.techconnect.opportunity.dto.ApiResponse;
import com.techconnect.opportunity.model.Notification;
import com.techconnect.opportunity.model.User;
import com.techconnect.opportunity.repository.UserRepository;
import com.techconnect.opportunity.service.AuthService;
import com.techconnect.opportunity.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    private final UserRepository userRepository;

    public NotificationController(NotificationService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getNotifications(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.of(200, "OK", service.getUserNotifications(user.getId())));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.of(200, "Marked as read", null));
    }
}

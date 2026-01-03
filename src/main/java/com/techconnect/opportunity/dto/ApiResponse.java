package com.techconnect.opportunity.dto;

import java.time.Instant;

public record ApiResponse(String timestamp, int status, String message, Object data) {
    public static ApiResponse of(int status, String message, Object data) {
        return new ApiResponse(Instant.now().toString(), status, message, data);
    }
}

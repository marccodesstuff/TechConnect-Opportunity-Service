package com.techconnect.opportunity.dto;

import java.time.LocalDateTime;

public record TeamRequestResponse(
        Long id,
        Long opportunityId,
        String username,
        String message,
        LocalDateTime createdAt) {
}

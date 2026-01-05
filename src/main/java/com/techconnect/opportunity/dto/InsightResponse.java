package com.techconnect.opportunity.dto;

import com.techconnect.opportunity.model.OpportunityInsight.Verdict;
import java.time.LocalDateTime;
import java.util.Set;

public record InsightResponse(
        Long id,
        Long opportunityId,
        String username,
        Verdict verdict,
        String comment,
        Set<String> tags,
        LocalDateTime createdAt) {
}

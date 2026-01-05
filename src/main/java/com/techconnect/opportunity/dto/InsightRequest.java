package com.techconnect.opportunity.dto;

import com.techconnect.opportunity.model.OpportunityInsight.Verdict;
import java.util.Set;

public record InsightRequest(
        Verdict verdict,
        String comment,
        Set<String> tags) {
}

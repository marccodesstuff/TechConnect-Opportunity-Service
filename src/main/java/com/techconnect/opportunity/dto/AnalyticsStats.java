package com.techconnect.opportunity.dto;

import java.util.Map;

public record AnalyticsStats(
        long totalOpportunities,
        long totalActiveOpportunities,
        Map<String, Long> opportunitiesByType) {
}

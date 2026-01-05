package com.techconnect.opportunity.dto;

import com.techconnect.opportunity.model.OpportunityType;
import java.time.LocalDate;
import java.util.List;

public record OpportunityResponse(
                Long id,
                String title,
                String description,
                LocalDate startDate,
                LocalDate endDate,
                OpportunityType type,
                List<String> tags) {
}

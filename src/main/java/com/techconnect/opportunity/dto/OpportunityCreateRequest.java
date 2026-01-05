package com.techconnect.opportunity.dto;

import com.techconnect.opportunity.model.OpportunityType;
import com.techconnect.opportunity.validation.DateRangeValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@DateRangeValid
public record OpportunityCreateRequest(
                @NotBlank String title,
                String description,
                @NotNull LocalDate startDate,
                @NotNull LocalDate endDate,
                @NotNull OpportunityType type,
                List<String> tags) {
}

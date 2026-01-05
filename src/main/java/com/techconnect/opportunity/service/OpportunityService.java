package com.techconnect.opportunity.service;

import com.techconnect.opportunity.dto.OpportunityCreateRequest;
import com.techconnect.opportunity.dto.OpportunityResponse;
import com.techconnect.opportunity.model.OpportunityType;
import java.util.List;

public interface OpportunityService {
    OpportunityResponse create(OpportunityCreateRequest request);

    OpportunityResponse getById(Long id);

    List<OpportunityResponse> listAll();

    List<OpportunityResponse> search(String keyword, OpportunityType type);

    void delete(Long id);
}

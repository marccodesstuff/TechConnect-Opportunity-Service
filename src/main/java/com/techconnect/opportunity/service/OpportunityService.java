package com.techconnect.opportunity.service;

import com.techconnect.opportunity.dto.OpportunityCreateRequest;
import com.techconnect.opportunity.dto.OpportunityResponse;

import java.util.List;

public interface OpportunityService {
    OpportunityResponse create(OpportunityCreateRequest request);
    OpportunityResponse getById(Long id);
    List<OpportunityResponse> listAll();
    void delete(Long id);
}

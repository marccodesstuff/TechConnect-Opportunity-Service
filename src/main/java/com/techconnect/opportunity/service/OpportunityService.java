package com.techconnect.opportunity.service;

import com.techconnect.opportunity.dto.AnalyticsStats;
import com.techconnect.opportunity.dto.InsightRequest;
import com.techconnect.opportunity.dto.InsightResponse;
import com.techconnect.opportunity.dto.OpportunityCreateRequest;
import com.techconnect.opportunity.dto.OpportunityResponse;
import com.techconnect.opportunity.dto.TeamRequestResponse;
import com.techconnect.opportunity.model.OpportunityType;
import java.util.List;

public interface OpportunityService {
    OpportunityResponse create(OpportunityCreateRequest request);

    OpportunityResponse getById(Long id);

    List<OpportunityResponse> listAll();

    List<OpportunityResponse> search(String keyword, OpportunityType type, String tag);

    void delete(Long id);

    AnalyticsStats getStats();

    // New methods
    TeamRequestResponse joinTeamLobby(Long opportunityId, String username, String message);

    void leaveTeamLobby(Long opportunityId, String username);

    List<TeamRequestResponse> getTeamRequests(Long opportunityId);

    InsightResponse addInsight(Long opportunityId, String username, InsightRequest request);

    List<InsightResponse> getInsights(Long opportunityId);
}

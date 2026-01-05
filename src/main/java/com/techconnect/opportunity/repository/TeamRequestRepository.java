package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.TeamRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {
    List<TeamRequest> findByOpportunityId(Long opportunityId);

    Optional<TeamRequest> findByOpportunityIdAndUserId(Long opportunityId, Long userId);
}

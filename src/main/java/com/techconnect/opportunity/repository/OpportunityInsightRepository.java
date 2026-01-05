package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.OpportunityInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpportunityInsightRepository extends JpaRepository<OpportunityInsight, Long> {
    List<OpportunityInsight> findByOpportunityId(Long opportunityId);
}

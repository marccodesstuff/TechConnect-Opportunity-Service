package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
}

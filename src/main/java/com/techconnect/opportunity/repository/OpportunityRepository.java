package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long>, JpaSpecificationExecutor<Opportunity> {
    List<Opportunity> findByEndDateBetween(LocalDate start, LocalDate end);
}

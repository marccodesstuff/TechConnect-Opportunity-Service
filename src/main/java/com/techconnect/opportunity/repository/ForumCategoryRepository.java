package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumCategoryRepository extends JpaRepository<ForumCategory, Long> {
}

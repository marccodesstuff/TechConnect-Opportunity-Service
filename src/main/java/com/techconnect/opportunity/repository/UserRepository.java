package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    java.util.List<User> findByFavorites_Id(Long opportunityId);
}

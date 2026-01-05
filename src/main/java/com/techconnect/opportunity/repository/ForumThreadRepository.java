package com.techconnect.opportunity.repository;

import com.techconnect.opportunity.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
    List<ForumThread> findByCategoryIdOrderByUpdatedAtDesc(Long categoryId);
}

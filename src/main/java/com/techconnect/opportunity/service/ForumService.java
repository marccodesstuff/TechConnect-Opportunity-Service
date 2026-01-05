package com.techconnect.opportunity.service;

import com.techconnect.opportunity.model.*;
import com.techconnect.opportunity.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ForumService {

    private final ForumCategoryRepository categoryRepository;
    private final ForumThreadRepository threadRepository;
    private final ForumPostRepository postRepository;
    private final UserRepository userRepository;

    public ForumService(ForumCategoryRepository categoryRepository,
            ForumThreadRepository threadRepository,
            ForumPostRepository postRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.threadRepository = threadRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<ForumCategory> getCategories() {
        if (categoryRepository.count() == 0) {
            // Seed defaults if empty
            categoryRepository.save(new ForumCategory("General", "General discussion", "general"));
            categoryRepository.save(new ForumCategory("Q&A", "Ask questions and get answers", "qa"));
            categoryRepository.save(new ForumCategory("Hackathons", "Discuss upcoming hackathons", "hackathons"));
        }
        return categoryRepository.findAll();
    }

    public List<ForumThread> getThreadsByCategory(Long categoryId) {
        return threadRepository.findByCategoryIdOrderByUpdatedAtDesc(categoryId);
    }

    public ForumThread getThread(Long id) {
        ForumThread thread = threadRepository.findById(id).orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.incrementViewCount();
        return threadRepository.save(thread);
    }

    public ForumThread createThread(Long categoryId, String username, String title, String content) {
        ForumCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ForumThread thread = new ForumThread(title, content, user, category);
        return threadRepository.save(thread);
    }

    public ForumPost createPost(Long threadId, String username, String content) {
        ForumThread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ForumPost post = new ForumPost(content, thread, user);
        post = postRepository.save(post);

        thread.setUpdatedAt(LocalDateTime.now());
        threadRepository.save(thread);

        return post;
    }
}

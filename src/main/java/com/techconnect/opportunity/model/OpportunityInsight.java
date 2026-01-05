package com.techconnect.opportunity.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "opportunity_insights")
public class OpportunityInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum Verdict {
        RECOMMENDED,
        MIXED,
        NOT_RECOMMENDED
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Verdict verdict;

    @Column(columnDefinition = "text")
    private String comment;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "insight_tags", joinColumns = @JoinColumn(name = "insight_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public OpportunityInsight() {
    }

    public OpportunityInsight(Opportunity opportunity, User user, Verdict verdict, String comment, Set<String> tags) {
        this.opportunity = opportunity;
        this.user = user;
        this.verdict = verdict;
        this.comment = comment;
        this.tags = tags;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public User getUser() {
        return user;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public String getComment() {
        return comment;
    }

    public Set<String> getTags() {
        return tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

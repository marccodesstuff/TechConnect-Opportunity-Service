package com.techconnect.opportunity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "forum_categories")
public class ForumCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, unique = true)
    private String slug;

    public ForumCategory() {
    }

    public ForumCategory(String name, String description, String slug) {
        this.name = name;
        this.description = description;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }
}

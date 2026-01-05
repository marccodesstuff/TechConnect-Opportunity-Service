package com.techconnect.opportunity.service.impl;

import com.techconnect.opportunity.dto.OpportunityCreateRequest;
import com.techconnect.opportunity.dto.OpportunityResponse;
import com.techconnect.opportunity.model.Opportunity;
import com.techconnect.opportunity.model.Tag;
import com.techconnect.opportunity.repository.OpportunityRepository;
import com.techconnect.opportunity.repository.TagRepository;
import com.techconnect.opportunity.service.OpportunityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import com.techconnect.opportunity.model.OpportunityType;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository repository;
    private final TagRepository tagRepository;

    public OpportunityServiceImpl(OpportunityRepository repository, TagRepository tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    public OpportunityResponse create(OpportunityCreateRequest request) {
        // Duplicate Check
        List<Opportunity> existing = repository.findAll(); // Optimization: In real app, filter by provider/date
        for (Opportunity o : existing) {
            if (com.techconnect.opportunity.util.SimilarityUtil.calculateSimilarity(o.getTitle(), request.title()) > 0.8
                    &&
                    com.techconnect.opportunity.util.SimilarityUtil.calculateSimilarity(o.getProvider(),
                            request.provider()) > 0.8) {
                throw new com.techconnect.opportunity.exception.DuplicateOpportunityException(
                        "Duplicate opportunity detected: " + o.getTitle());
            }
        }

        Opportunity.Builder builder = Opportunity.builder()
                .title(request.title())
                .description(request.description())
                .provider(request.provider())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .type(request.type());

        Opportunity opp = builder.build();

        if (request.tags() != null && !request.tags().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : request.tags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                tags.add(tag);
            }
            opp.setTags(tags);
        }

        Opportunity saved = repository.save(opp);
        return toResponse(saved);
    }

    @Override
    public OpportunityResponse getById(Long id) {
        Opportunity opp = repository.findById(id).orElseThrow(() -> new RuntimeException("Opportunity not found"));
        return toResponse(opp);
    }

    @Override
    public List<OpportunityResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<OpportunityResponse> search(String keyword, OpportunityType type, String tag) {
        Specification<Opportunity> spec = Specification.where(null);

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }

        if (StringUtils.hasText(tag)) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                return cb.equal(root.join("tags").get("name"), tag);
            });
        }

        if (StringUtils.hasText(keyword)) {
            String pattern = "%" + keyword.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)));
        }

        return repository.findAll(spec).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public com.techconnect.opportunity.dto.AnalyticsStats getStats() {
        List<Opportunity> all = repository.findAll();
        long total = all.size();
        long active = all.stream().filter(o -> o.getEndDate().isAfter(java.time.LocalDate.now())).count();
        java.util.Map<String, Long> byType = all.stream()
                .collect(Collectors.groupingBy(o -> o.getType().name(), Collectors.counting()));

        return new com.techconnect.opportunity.dto.AnalyticsStats(total, active, byType);
    }

    private OpportunityResponse toResponse(Opportunity o) {
        List<String> tags = o.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        return new OpportunityResponse(o.getId(), o.getTitle(), o.getDescription(), o.getProvider(), o.getStartDate(),
                o.getEndDate(),
                o.getType(), tags);
    }
}

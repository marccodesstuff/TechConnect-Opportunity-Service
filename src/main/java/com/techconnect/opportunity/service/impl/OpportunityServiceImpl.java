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
    private final com.techconnect.opportunity.repository.TeamRequestRepository teamRequestRepository;
    private final com.techconnect.opportunity.repository.OpportunityInsightRepository insightRepository;
    private final com.techconnect.opportunity.repository.UserRepository userRepository;

    public OpportunityServiceImpl(OpportunityRepository repository,
            TagRepository tagRepository,
            com.techconnect.opportunity.repository.TeamRequestRepository teamRequestRepository,
            com.techconnect.opportunity.repository.OpportunityInsightRepository insightRepository,
            com.techconnect.opportunity.repository.UserRepository userRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.teamRequestRepository = teamRequestRepository;
        this.insightRepository = insightRepository;
        this.userRepository = userRepository;
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

    @Override
    public com.techconnect.opportunity.dto.TeamRequestResponse joinTeamLobby(Long opportunityId, String username,
            String message) {
        Opportunity opp = repository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (opp.getType() != OpportunityType.HACKATHON) {
            throw new RuntimeException("Team formation only available for Hackathons");
        }

        com.techconnect.opportunity.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already in lobby
        if (teamRequestRepository.findByOpportunityIdAndUserId(opportunityId, user.getId()).isPresent()) {
            throw new RuntimeException("User already in team lobby");
        }

        com.techconnect.opportunity.model.TeamRequest request = new com.techconnect.opportunity.model.TeamRequest(opp,
                user, message);
        request = teamRequestRepository.save(request);

        return new com.techconnect.opportunity.dto.TeamRequestResponse(
                request.getId(), request.getOpportunity().getId(), request.getUser().getUsername(),
                request.getMessage(), request.getCreatedAt());
    }

    @Override
    public void leaveTeamLobby(Long opportunityId, String username) {
        com.techconnect.opportunity.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.techconnect.opportunity.model.TeamRequest request = teamRequestRepository
                .findByOpportunityIdAndUserId(opportunityId, user.getId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        teamRequestRepository.delete(request);
    }

    @Override
    public List<com.techconnect.opportunity.dto.TeamRequestResponse> getTeamRequests(Long opportunityId) {
        return teamRequestRepository.findByOpportunityId(opportunityId).stream()
                .map(r -> new com.techconnect.opportunity.dto.TeamRequestResponse(
                        r.getId(), r.getOpportunity().getId(), r.getUser().getUsername(), r.getMessage(),
                        r.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public com.techconnect.opportunity.dto.InsightResponse addInsight(Long opportunityId, String username,
            com.techconnect.opportunity.dto.InsightRequest request) {
        Opportunity opp = repository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (opp.getType() == OpportunityType.HACKATHON) {
            throw new RuntimeException("Insights not available for Hackathons");
        }

        com.techconnect.opportunity.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.techconnect.opportunity.model.OpportunityInsight insight = new com.techconnect.opportunity.model.OpportunityInsight(
                opp, user, request.verdict(), request.comment(), request.tags());

        insight = insightRepository.save(insight);

        return new com.techconnect.opportunity.dto.InsightResponse(
                insight.getId(), insight.getOpportunity().getId(), insight.getUser().getUsername(),
                insight.getVerdict(), insight.getComment(), insight.getTags(), insight.getCreatedAt());
    }

    @Override
    public List<com.techconnect.opportunity.dto.InsightResponse> getInsights(Long opportunityId) {
        return insightRepository.findByOpportunityId(opportunityId).stream()
                .map(i -> new com.techconnect.opportunity.dto.InsightResponse(
                        i.getId(), i.getOpportunity().getId(), i.getUser().getUsername(),
                        i.getVerdict(), i.getComment(), i.getTags(), i.getCreatedAt()))
                .collect(Collectors.toList());
    }

    private OpportunityResponse toResponse(Opportunity o) {
        List<String> tags = o.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        return new OpportunityResponse(o.getId(), o.getTitle(), o.getDescription(), o.getProvider(), o.getStartDate(),
                o.getEndDate(),
                o.getType(), tags);
    }
}

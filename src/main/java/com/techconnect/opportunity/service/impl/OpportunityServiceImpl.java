package com.techconnect.opportunity.service.impl;

import com.techconnect.opportunity.dto.OpportunityCreateRequest;
import com.techconnect.opportunity.dto.OpportunityResponse;
import com.techconnect.opportunity.model.Opportunity;
import com.techconnect.opportunity.repository.OpportunityRepository;
import com.techconnect.opportunity.service.OpportunityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import com.techconnect.opportunity.model.OpportunityType;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository repository;

    public OpportunityServiceImpl(OpportunityRepository repository) {
        this.repository = repository;
    }

    @Override
    public OpportunityResponse create(OpportunityCreateRequest request) {
        Opportunity opp = Opportunity.builder()
                .title(request.title())
                .description(request.description())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .type(request.type())
                .build();
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
    public List<OpportunityResponse> search(String keyword, OpportunityType type) {
        Specification<Opportunity> spec = Specification.where(null);

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
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

    private OpportunityResponse toResponse(Opportunity o) {
        return new OpportunityResponse(o.getId(), o.getTitle(), o.getDescription(), o.getStartDate(), o.getEndDate(),
                o.getType());
    }
}

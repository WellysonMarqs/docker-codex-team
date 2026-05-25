package com.customizationaudit.divergence.application;

import com.customizationaudit.divergence.domain.Divergence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ListDivergencesQuery {

    private final DivergenceRepository divergenceRepository;

    public ListDivergencesQuery(DivergenceRepository divergenceRepository) {
        this.divergenceRepository = divergenceRepository;
    }

    @Transactional(readOnly = true)
    public List<Divergence> list() {
        return list(null, null);
    }

    @Transactional(readOnly = true)
    public List<Divergence> list(UUID customerId, UUID environmentId) {
        return load(customerId, environmentId).stream()
                .sorted(Comparator.comparing(Divergence::detectedAt, Comparator.reverseOrder()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Divergence get(UUID divergenceId) {
        return divergenceRepository.findById(divergenceId)
                .orElseThrow(() -> new IllegalArgumentException("divergence not found"));
    }

    private List<Divergence> load(UUID customerId, UUID environmentId) {
        if (customerId != null && environmentId != null) {
            return divergenceRepository.findByCustomerIdAndEnvironmentId(customerId, environmentId);
        }
        if (customerId != null) {
            return divergenceRepository.findByCustomerId(customerId);
        }
        if (environmentId != null) {
            return divergenceRepository.findByEnvironmentId(environmentId);
        }
        return divergenceRepository.findAll();
    }
}

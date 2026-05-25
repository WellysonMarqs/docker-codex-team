package com.customizationaudit.divergence.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class UpdateDivergenceStatusService {

    private final DivergenceRepository divergenceRepository;
    private final Clock clock;

    public UpdateDivergenceStatusService(DivergenceRepository divergenceRepository, Clock clock) {
        this.divergenceRepository = divergenceRepository;
        this.clock = clock;
    }

    @Transactional
    public DivergenceView update(UpdateDivergenceStatusCommand command) {
        var divergence = divergenceRepository.findById(command.divergenceId())
                .orElseThrow(() -> new IllegalArgumentException("divergence not found"));

        var updated = divergence.transitionTo(command.status(), clock.instant());
        return DivergenceView.from(divergenceRepository.save(updated));
    }
}

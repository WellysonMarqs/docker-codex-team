package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ListVerificationRunsQuery {

    private final VerificationRunRepository verificationRunRepository;
    private final VerificationResultRepository verificationResultRepository;

    public ListVerificationRunsQuery(
            VerificationRunRepository verificationRunRepository,
            VerificationResultRepository verificationResultRepository
    ) {
        this.verificationRunRepository = verificationRunRepository;
        this.verificationResultRepository = verificationResultRepository;
    }

    @Transactional(readOnly = true)
    public List<VerificationExecution> list() {
        return verificationRunRepository.findAll().stream()
                .sorted(Comparator.comparing(run -> run.startedAt(), Comparator.reverseOrder()))
                .map(run -> new VerificationExecution(
                        run,
                        loadResult(run.id())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public VerificationExecution get(UUID verificationRunId) {
        var run = verificationRunRepository.findById(verificationRunId)
                .orElseThrow(() -> new IllegalArgumentException("verification run not found"));
        return new VerificationExecution(run, loadResult(verificationRunId));
    }

    private VerificationResult loadResult(UUID verificationRunId) {
        return verificationResultRepository.findByVerificationRunId(verificationRunId).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("verification result not found"));
    }
}

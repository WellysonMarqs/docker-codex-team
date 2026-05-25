package com.customizationaudit.verification.application;

import com.customizationaudit.verification.domain.VerificationResult;
import com.customizationaudit.verification.domain.VerificationRun;

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
        return list(null, null);
    }

    @Transactional(readOnly = true)
    public List<VerificationExecution> list(UUID customerId, UUID environmentId) {
        return loadRuns(customerId, environmentId).stream()
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

    private List<VerificationRun> loadRuns(UUID customerId, UUID environmentId) {
        if (customerId != null && environmentId != null) {
            return verificationRunRepository.findByCustomerIdAndEnvironmentId(customerId, environmentId);
        }
        if (customerId != null) {
            return verificationRunRepository.findByCustomerId(customerId);
        }
        if (environmentId != null) {
            return verificationRunRepository.findByEnvironmentId(environmentId);
        }
        return verificationRunRepository.findAll();
    }
}

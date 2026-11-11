package com.enterprise.fashion.ecommerce.product.application.media;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/** Immutable technical observation. It is never authoritative Product publication evidence. */
public record ProductMediaSecurityCheckResult(UUID checkId, UUID assetId, ProductId productId,
        String originalSha256, String checkDefinition, String implementationVersion, Instant executedAt,
        Outcome outcome, FailureReason failureReason, MalwareScan malwareScan,
        boolean authoritativeAcceptanceEligible) {
    public ProductMediaSecurityCheckResult {
        Objects.requireNonNull(checkId, "checkId"); Objects.requireNonNull(assetId, "assetId");
        Objects.requireNonNull(productId, "productId"); Objects.requireNonNull(executedAt, "executedAt");
        Objects.requireNonNull(outcome, "outcome"); Objects.requireNonNull(malwareScan, "malwareScan");
        if (originalSha256 == null || !originalSha256.matches("[0-9a-f]{64}")) throw new IllegalArgumentException("Invalid digest");
        if (checkDefinition == null || checkDefinition.isBlank() || implementationVersion == null || implementationVersion.isBlank()) {
            throw new IllegalArgumentException("Check attribution is required");
        }
        if ((outcome == Outcome.REJECTED) != (failureReason != null)) throw new IllegalArgumentException("Invalid outcome detail");
        if (malwareScan != MalwareScan.NOT_PERFORMED || authoritativeAcceptanceEligible) {
            throw new IllegalArgumentException("Technical checking cannot establish authoritative acceptance");
        }
    }
    public enum Outcome { TECHNICAL_CHECKS_PASSED, REJECTED }
    public enum FailureReason { MALFORMED_CONTENT, UNSUPPORTED_FORMAT, RESOURCE_LIMIT_EXCEEDED }
    public enum MalwareScan { NOT_PERFORMED }
}

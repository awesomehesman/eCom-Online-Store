package com.enterprise.fashion.ecommerce.identity.application.credential;

/**
 * VERIFIED means password match for the exact source binding at the final applicability check.
 * It is neither Authentication, Principal, Session, Authorization nor a permanent currentness
 * guarantee. No result carries secret material or represents confirmed absence.
 */
public enum SourceBoundPasswordVerificationResult {
    VERIFIED, NO_MATCH, REJECTED, SOURCE_NON_CURRENT, SOURCE_CONFLICT, SOURCE_UNCERTAIN
}

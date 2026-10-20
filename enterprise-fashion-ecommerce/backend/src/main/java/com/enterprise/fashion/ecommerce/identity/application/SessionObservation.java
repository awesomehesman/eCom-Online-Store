package com.enterprise.fashion.ecommerce.identity.application;

/** Point-in-time store observation, never Authentication or Authorization evidence. */
public enum SessionObservation {
    PRESENT,
    /** No unexpired record was observed; no absence reason is inferred. */
    ABSENT,
    UNCERTAIN
}

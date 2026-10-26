package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class LogoutResponses {
    private final ObjectMapper json;

    public LogoutResponses(ObjectMapper json) {
        this.json = json;
    }

    public void headers(HttpServletRequest request, HttpServletResponse response) {
        String correlation = request.getHeader("X-Correlation-ID");
        if (correlation == null || !correlation.matches("[A-Za-z0-9._~-]{1,128}")) {
            correlation = UUID.randomUUID().toString();
        }
        response.setHeader("X-Correlation-ID", correlation);
        response.setHeader("Cache-Control", "no-store");
    }

    public void problem(HttpServletResponse response, int status) throws IOException {
        String title = switch (status) {
            case 400 -> "Bad Request";
            case 403 -> "Forbidden";
            case 500 -> "Internal Server Error";
            case 503 -> "Service Unavailable";
            default -> throw new IllegalArgumentException("Unsupported logout outcome");
        };
        response.setStatus(status);
        response.setContentType("application/problem+json");
        json.writeValue(response.getOutputStream(), Map.of("type", "about:blank", "title", title, "status", status));
    }

    public void token(HttpServletResponse response, String token) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json");
        json.writeValue(response.getOutputStream(), Map.of("token", token));
    }
}

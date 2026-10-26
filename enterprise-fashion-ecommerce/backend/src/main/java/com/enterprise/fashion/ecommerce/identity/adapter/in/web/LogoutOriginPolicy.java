package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;

/** Fail closed; request headers never define the trusted public origin. */
public final class LogoutOriginPolicy {
    private final URI trusted;

    public LogoutOriginPolicy(String configuredOrigin) {
        this.trusted = origin(configuredOrigin, false);
    }

    public boolean allows(HttpServletRequest request) {
        if (trusted == null || !"https".equalsIgnoreCase(trusted.getScheme())) {
            return false;
        }
        String origin = single(request, "Origin");
        String referer = single(request, "Referer");
        String fetch = single(request, "Sec-Fetch-Site");
        if (origin == null && referer == null) {
            return false;
        }
        if ((origin != null && !matches(origin(origin, false)))
                || (referer != null && !matches(origin(referer, true)))) {
            return false;
        }
        return fetch == null || Set.of("same-origin", "none").contains(fetch);
    }

    private boolean matches(URI candidate) {
        return candidate != null && trusted.getScheme().equalsIgnoreCase(candidate.getScheme())
                && trusted.getHost().equalsIgnoreCase(candidate.getHost())
                && port(trusted) == port(candidate);
    }

    private static int port(URI uri) {
        return uri.getPort() == -1 ? ("https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80) : uri.getPort();
    }

    private static String single(HttpServletRequest request, String name) {
        var values = Collections.list(request.getHeaders(name));
        return values.isEmpty() ? null : values.size() == 1 ? values.getFirst() : "invalid multiple values";
    }

    private static URI origin(String value, boolean referer) {
        if (value == null) {
            return null;
        }
        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            if (scheme == null || !Set.of("http", "https").contains(scheme.toLowerCase(Locale.ROOT))
                    || uri.getHost() == null || uri.getRawUserInfo() != null || uri.getFragment() != null
                    || uri.getPort() < -1 || uri.getPort() > 65535
                    || (!referer && (!uri.getRawPath().isEmpty() || uri.getRawQuery() != null))) {
                return null;
            }
            return uri;
        }
        catch (URISyntaxException exception) {
            return null;
        }
    }
}

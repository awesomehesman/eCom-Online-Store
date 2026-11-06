package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import java.util.Arrays;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;

/** Shared resolver; strict diagnostics apply only to the logout interaction. */
public final class LogoutSessionIdResolver implements HttpSessionIdResolver {
    private final String cookieName;
    private final CookieHttpSessionIdResolver delegate = new CookieHttpSessionIdResolver();

    public LogoutSessionIdResolver(String cookieName) {
        this.cookieName = cookieName;
        var serializer = new DefaultCookieSerializer();
        serializer.setCookieName(cookieName);
        delegate.setCookieSerializer(serializer);
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        List<String> ids = delegate.resolveSessionIds(request);
        if ("POST".equals(request.getMethod())
                && (request.getContextPath() + "/api/v1/identity/logout").equals(request.getRequestURI())) {
            long supplied = request.getCookies() == null ? 0 : Arrays.stream(request.getCookies())
                    .filter(cookie -> cookieName.equals(cookie.getName())).count();
            // DefaultCookieSerializer omits undecodable values. Never call that absence.
            if (supplied != ids.size() || ids.size() > 1) {
                throw new InvalidCredential();
            }
        }
        return ids;
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String id) {
        delegate.setSessionId(request, response, id);
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        delegate.expireSession(request, response);
    }

    public static final class InvalidCredential extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;
        public InvalidCredential() {
            super("Invalid current Session credential");
        }
    }
}

package com.example.time.box.security;

import com.example.time.box.entity.SessionEntity;
import com.example.time.box.repository.SessionRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class SessionAuthFilter implements Filter {

    private final SessionRepository sessionRepository;

    public SessionAuthFilter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        if (uri.startsWith("/auth/") || uri.startsWith("/actuator/") || uri.startsWith("/ws-chat/") || uri.startsWith("/topic/") || uri.startsWith("/app/")) {
            chain.doFilter(request, response);
            return;
        }

        String sessionKey = httpRequest.getHeader("X-Session-Key");
        if (sessionKey == null || sessionKey.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        SessionEntity session = sessionRepository.findBySessionKey(sessionKey).orElse(null);
        if (session == null || session.getExpiredAt() == null || session.getExpiredAt().isBefore(OffsetDateTime.now())) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Authentication auth = new AbstractAuthenticationToken(List.of(new SimpleGrantedAuthority("ROLE_USER"))) {
            @Override
            public Object getCredentials() { return sessionKey; }
            @Override
            public Object getPrincipal() { return session.getUserId(); }
        };
        ((AbstractAuthenticationToken) auth).setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}



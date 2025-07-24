package com.example.time.box.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomLogoutFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CustomLogoutFilter customLogoutFilter;

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void doFilterInternal_LogoutRequest_InvalidatesSessionAndDeletesCookie() throws ServletException, IOException {
        // Setup
        when(request.getRequestURI()).thenReturn("/sessions/");
        when(request.getMethod()).thenReturn("DELETE");

        // Execute
        customLogoutFilter.doFilter(request, response, filterChain);

        // Verify
        verify(session, times(1)).invalidate();
        verify(response, times(1)).addCookie(any(Cookie.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(filterChain, never()).doFilter(request, response);
    }

}
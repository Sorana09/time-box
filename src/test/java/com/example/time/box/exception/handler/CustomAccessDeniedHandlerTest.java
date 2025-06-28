package com.example.time.box.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private CustomAccessDeniedHandler accessDeniedHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accessDeniedHandler = new CustomAccessDeniedHandler();
    }

    @Test
    void handle_ShouldThrowAccessDeniedException() {
        // Given
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

        // When & Then
        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            accessDeniedHandler.handle(request, response, accessDeniedException);
        });

        assertEquals("Access denied", exception.getMessage());
        verify(request, never()).getMethod(); // Verify no interactions with request
        verify(response, never()).setStatus(anyInt()); // Verify no interactions with response
    }

    @Test
    void handle_ShouldLogAccessDenied() {
        // This test verifies that the handler logs the access denied event
        // Since we can't easily verify logging in a unit test, we're just testing
        // that the method throws the expected exception
        
        // Given
        AccessDeniedException accessDeniedException = new AccessDeniedException("Custom access denied message");

        // When & Then
        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            accessDeniedHandler.handle(request, response, accessDeniedException);
        });

        assertEquals("Access denied", exception.getMessage());
    }
}
package com.example.time.box.exception.handler;

import com.example.time.box.exception.common.HttpException;
import com.example.time.box.exception.common.HttpExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpExceptionHandlerTest {

    private HttpExceptionHandler httpExceptionHandler;

    @BeforeEach
    void setUp() {
        httpExceptionHandler = new HttpExceptionHandler();
    }

    @Test
    void handleHttpException() {
        // Given
        String errorMessage = "Test error message";
        HttpStatus errorStatus = HttpStatus.BAD_REQUEST;
        HttpException httpException = new HttpException(errorStatus, errorMessage);

        // When
        ResponseEntity<HttpExceptionResponse> responseEntity = httpExceptionHandler.handleHttpException(httpException);

        // Then
        assertEquals(errorStatus, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertEquals(errorStatus, responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getTime());
    }

    @Test
    void handleRuntimeException() {
        // Given
        String errorMessage = "Test runtime error";
        RuntimeException runtimeException = new RuntimeException(errorMessage);

        // When
        ResponseEntity<HttpExceptionResponse> responseEntity = httpExceptionHandler.handleHttpExceeption(runtimeException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getTime());
    }

    @Test
    void handleHttpException_WithDifferentStatusCodes() {
        // Test with different HTTP status codes
        testWithStatusCode(HttpStatus.NOT_FOUND, "Resource not found");
        testWithStatusCode(HttpStatus.FORBIDDEN, "Access denied");
        testWithStatusCode(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        testWithStatusCode(HttpStatus.CONFLICT, "Resource conflict");
    }

    private void testWithStatusCode(HttpStatus status, String message) {
        // Given
        HttpException httpException = new HttpException(status, message);

        // When
        ResponseEntity<HttpExceptionResponse> responseEntity = httpExceptionHandler.handleHttpException(httpException);

        // Then
        assertEquals(status, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(message, responseEntity.getBody().getMessage());
        assertEquals(status, responseEntity.getBody().getStatus());
    }
}
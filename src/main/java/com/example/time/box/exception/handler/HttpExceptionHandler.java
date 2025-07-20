package com.example.time.box.exception.handler;


import com.example.time.box.exception.common.HttpException;
import com.example.time.box.exception.common.HttpExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(HttpException.class)
    ResponseEntity<HttpExceptionResponse> handleHttpException(final HttpException e) {
        return ResponseEntity.status(e.getStatus()).body(new HttpExceptionResponse(e));
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<HttpExceptionResponse> handleHttpExceeption(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new HttpExceptionResponse(new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<HttpExceptionResponse> handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException e) {
        log.warn("Unsupported media type: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                new HttpExceptionResponse(new HttpException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
                "Unsupported media type. Please use application/json for request body or application/x-www-form-urlencoded for form data."))
        );
    }

}

package com.example.time.box.exception.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
public class HttpExceptionResponse {
    private String message;
    private HttpStatus status;
    private OffsetDateTime time;

    public HttpExceptionResponse(final HttpException httpException){
        this.message = httpException.getMessage();
        this.status = httpException.getStatus();
        this.time = OffsetDateTime.now();
    }

}

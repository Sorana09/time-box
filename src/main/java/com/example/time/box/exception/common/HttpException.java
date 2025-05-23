package com.example.time.box.exception.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class HttpException extends RuntimeException {

    final private HttpStatus status;

    public HttpException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }
}
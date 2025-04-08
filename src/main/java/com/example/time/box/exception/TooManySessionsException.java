package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class TooManySessionsException extends HttpException {
    public TooManySessionsException() {
        super(HttpStatus.FORBIDDEN, "Too many sessions");
    }
}

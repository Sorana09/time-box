package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends HttpException {
    public IncorrectPasswordException() {
        super(HttpStatus.CONFLICT, "Incorrect password");
    }
}

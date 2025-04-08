package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyRegisteredException extends HttpException {
    public EmailAlreadyRegisteredException() {
        super(HttpStatus.CONFLICT, "Email already registered");
    }
}

package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class EmailisNotRegisteredException extends HttpException {
    public EmailisNotRegisteredException() {
        super(HttpStatus.NOT_FOUND, "Email is not registered");
    }
}

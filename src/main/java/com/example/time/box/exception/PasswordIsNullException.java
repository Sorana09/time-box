package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class PasswordIsNullException extends HttpException {
    public PasswordIsNullException() {
        super(HttpStatus.BAD_REQUEST, "Password is required");
    }
}

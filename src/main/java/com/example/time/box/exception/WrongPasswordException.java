package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends HttpException {
    public WrongPasswordException() {
        super(HttpStatus.UNAUTHORIZED, "Wrong password");
    }
}

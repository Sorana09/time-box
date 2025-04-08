package com.example.time.box.exception;

import com.example.time.box.exception.common.HttpException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends HttpException {
    public EntityNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Entity not found");
    }
}

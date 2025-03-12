package com.pcs.limitless_growth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MissionNotFoundException extends RuntimeException {
    public MissionNotFoundException(String message) {
        super(message);
    }
}

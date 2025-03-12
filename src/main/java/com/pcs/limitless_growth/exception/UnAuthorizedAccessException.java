package com.pcs.limitless_growth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedAccessException extends RuntimeException {
    public UnAuthorizedAccessException(String message) {
        super(message);
    }
}

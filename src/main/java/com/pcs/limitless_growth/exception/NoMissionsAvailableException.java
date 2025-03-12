package com.pcs.limitless_growth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoMissionsAvailableException extends RuntimeException {
    public NoMissionsAvailableException(String s) {
        super(s);
    }
}





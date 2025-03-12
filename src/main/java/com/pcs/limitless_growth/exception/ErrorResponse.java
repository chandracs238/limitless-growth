package com.pcs.limitless_growth.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}

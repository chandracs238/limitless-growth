package com.pcs.limitless_growth.exception;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}

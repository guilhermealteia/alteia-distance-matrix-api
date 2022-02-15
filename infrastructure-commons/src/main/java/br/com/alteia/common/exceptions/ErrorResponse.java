package br.com.alteia.common.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(HttpStatus httpStatus, String code, List<String> details) {

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public List<String> getDetails() {
        return details;
    }
}

package com.ecommerce.project.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class APIException extends RuntimeException {

    private  HttpStatus httpStatus;
    private String message;
    @Serial
    private static final long serialVersionUID=1L;

    public APIException(String message) {
        this.message = message;
    }

    public APIException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

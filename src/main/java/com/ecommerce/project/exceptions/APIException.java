package com.ecommerce.project.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
public class APIException extends RuntimeException {

    private  HttpStatus httpStatus;
    private final List<String> errorMessage=new ArrayList<>();
    @Serial
    private static final long serialVersionUID=1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, HttpStatus status) {
        super(message);
        this.httpStatus=status;
        this.errorMessage.add(message);
    }
}

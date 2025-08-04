package com.ecommerce.project.exceptions;

import com.ecommerce.project.response.RestApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<RestApiResponse<Object>> handleRestApiException(RestApiException ex, WebRequest request){
        return new ResponseEntity<>(RestApiResponse.builder().message(ex.getMessage())
                .errors(request.getDescription(false))
                .timestamp(new Date())
                .build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiResponse<Object>> handleMethodValidationException(MethodArgumentNotValidException ex, WebRequest request){
        List<String> errors=new ArrayList<>();
        for(FieldError error: ex.getBindingResult().getFieldErrors()){
            errors.add(error.getDefaultMessage());
        }
        Collections.sort(errors);
        return new ResponseEntity<>(RestApiResponse.builder().message(errors)
                .errors(request.getDescription(false))
                .timestamp(new Date())
                .build(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request){
        List<String> errors=new ArrayList<>();
        for(ConstraintViolation<?> violation:ex.getConstraintViolations()){
            errors.add(violation.getMessage());
        }
        Collections.sort(errors);
        return new ResponseEntity<>(RestApiResponse.builder()
                .message(errors)
                .errors(request.getDescription(false))
                .timestamp(new Date())
                .build(),HttpStatus.BAD_REQUEST);
    }

}


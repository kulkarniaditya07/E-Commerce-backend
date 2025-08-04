package com.ecommerce.project.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RestApiException extends RuntimeException {
  private final List<String> errors = new ArrayList<>();
  private final HttpStatus httpStatus;



  public RestApiException(Object message, HttpStatus httpStatus){
      super(message.toString());
      this.httpStatus = httpStatus;
      this.errors.add(message.toString());
  }

  public RestApiException(String message, HttpStatus httpStatus){
      super(message);
      this.httpStatus = httpStatus;
      this.errors.add(message);
  }

  public RestApiException(List<String> message, HttpStatus httpStatus){
      super(message.toString());
      this.httpStatus = httpStatus;
      this.errors.addAll(message);
  }

}

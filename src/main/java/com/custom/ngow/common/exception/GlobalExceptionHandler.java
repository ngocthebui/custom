package com.custom.ngow.common.exception;

import com.custom.ngow.common.dto.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ForwardException.class)
  public ResponseEntity<ExceptionResponse> handlingForwardException(ForwardException exception) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setResponseCode(exception.getErrorCode().name());
    exceptionResponse.setResponseMessage(exception.getMessage());

    return ResponseEntity
        .status(exception.getErrorCode().getValue())
        .body(exceptionResponse);
  }
}

package com.custom.ngow.common.exception;

import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.dto.response.ExceptionResponse;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setResponseCode(ErrorCode.E400101.name());
        exceptionResponse.setResponseMessage(ErrorCode.E400101.getMessage() +
                Objects.requireNonNull(exception.getFieldError()).getField());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }
}

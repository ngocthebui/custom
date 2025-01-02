package com.custom.ngow.common.exception;

import com.custom.ngow.common.constant.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForwardException extends RuntimeException {

    private final ErrorCode errorCode;

    public ForwardException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ForwardException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}

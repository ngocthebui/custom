package com.custom.ngow.shop.exception;

import com.custom.ngow.shop.common.MessageUtil;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  public CustomException(MessageUtil messageUtil, String errorCode) {
    super(messageUtil.getMessage(errorCode));
  }

  public CustomException(MessageUtil messageUtil, String errorCode, Object[] arg) {
    super(messageUtil.getMessage(errorCode, arg));
  }

  public CustomException(String message) {
    super(message);
  }

}

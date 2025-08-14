package com.custom.ngow.shop.exception;

import com.custom.ngow.shop.common.MessageUtil;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final String errorCode;
  private final Object[] args;
  private final String field;

  public CustomException(MessageUtil messageUtil, String field, String errorCode) {
    super(messageUtil.getMessage(errorCode));
    this.errorCode = errorCode;
    this.args = null;
    this.field = field;
  }

  public CustomException(MessageUtil messageUtil, String field, Object[] arg, String errorCode) {
    super(messageUtil.getMessage(errorCode, arg));
    this.errorCode = errorCode;
    this.args = arg;
    this.field = field;
  }

  public CustomException(String message) {
    super(message);
    this.errorCode = null;
    this.args = null;
    this.field = null;
  }
}

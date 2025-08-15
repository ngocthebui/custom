package com.custom.ngow.shop.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageUtil {
  private final MessageSource messageSource;

  public String getMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }

  public String getMessage(String key, Object[] args) {
    return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}

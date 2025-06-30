package com.custom.ngow.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

  @GetMapping("/test-error")
  public String testError() {
    log.error("This is a test error log to check alerting.",
        new RuntimeException("Test Exception Details"));
    return "Test error log has been generated!";
  }
}

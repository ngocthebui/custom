package com.custom.ngow.common.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthCheckController {

  @GetMapping
  public ResponseEntity<String> checkHealth() {
    ZoneId serverTimeZone = ZoneId.systemDefault();
    ZonedDateTime now = ZonedDateTime.now(serverTimeZone);

    String formattedDateTime = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z"));

    log.info("Health check: {} (Server timezone: {})", formattedDateTime, serverTimeZone.getId());

    String response = String.format(
        "Health check successful!\nCurrent time: %s\nServer timezone: %s",
        formattedDateTime,
        serverTimeZone.getId()
    );

    return ResponseEntity.ok(response);
  }
}

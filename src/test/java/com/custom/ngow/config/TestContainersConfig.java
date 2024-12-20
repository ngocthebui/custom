package com.custom.ngow.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
@Slf4j
public class TestContainersConfig {

  @Container
  private static final MySQLContainer<?> mySQLContainer;

  static {
    mySQLContainer = new MySQLContainer<>("mysql:8.0.33")
        .withDatabaseName("custom")
        .withUsername("root")
        .withPassword("root")
        .withStartupTimeout(Duration.ofMinutes(10L));

    mySQLContainer.start();

    JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(mySQLContainer, "");
    ScriptUtils.runInitScript(containerDelegate, "mysql/mysql_schema.sql");
    ScriptUtils.runInitScript(containerDelegate, "mysql/mysql_data.sql");
    log.info("Create databases test");
  }

  @DynamicPropertySource
  static void registerDynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }
}
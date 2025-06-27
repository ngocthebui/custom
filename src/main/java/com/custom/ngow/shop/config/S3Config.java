package com.custom.ngow.shop.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Value("${aws.s3.access-key}")
  private String accessKey;

  @Value("${aws.s3.secret-key}")
  private String secretKey;

  @Value("${aws.s3.region}")
  private String region;

  @Value("${aws.s3.cdn-url}")
  private String endpointUrl;

  @Bean
  public S3Client s3Client() throws URISyntaxException {
    return S3Client.builder()
        .endpointOverride(new URI(endpointUrl))
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .forcePathStyle(true)
        .build();
  }
}

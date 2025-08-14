package com.madiest.moapin.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@TestConfiguration
public class TestConfig {

  @Bean
  public S3Presigner s3Presigner() {
    return mock(S3Presigner.class);
  }
}

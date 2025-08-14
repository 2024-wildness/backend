package com.madiest.moapin.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

public class TestContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    GenericApplicationContext context = (GenericApplicationContext) applicationContext;
    context.registerBean(S3Presigner.class, () -> mock(S3Presigner.class));
  }
}

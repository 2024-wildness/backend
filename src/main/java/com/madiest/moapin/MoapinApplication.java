package com.madiest.moapin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Scan all application packages so that @ConfigurationProperties classes like
// com.madiest.moapin.common.config.AppProperties are registered.
@org.springframework.boot.context.properties.ConfigurationPropertiesScan(
  basePackages = "com.madiest.moapin")
@org.springframework.scheduling.annotation.EnableScheduling
public class MoapinApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoapinApplication.class, args);
  }
}

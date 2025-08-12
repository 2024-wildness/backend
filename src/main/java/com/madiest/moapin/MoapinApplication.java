package com.madiest.moapin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.boot.context.properties.ConfigurationPropertiesScan("com.madiest.moapin.config")
@org.springframework.scheduling.annotation.EnableScheduling
public class MoapinApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoapinApplication.class, args);
    }

}
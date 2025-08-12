package com.madiest.config;

import com.madiest.moapin.MoapinApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MoapinApplication.class)
@ActiveProfiles("test")
@DisplayName("Application Context Validation Tests")

class ApplicationContextValidationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private S3Presigner s3Presigner;

    @Test
    @DisplayName("Should load application context successfully")
    void shouldLoadApplicationContextSuccessfully() {
        assertNotNull(applicationContext, "Application context should be loaded");
        assertTrue(applicationContext.getBeanDefinitionCount() > 0, 
                  "Application context should contain bean definitions");
    }

    @Test
    @DisplayName("Should have Spring Boot auto-configuration beans")
    void shouldHaveSpringBootAutoConfigurationBeans() {
        assertAll("Essential Spring Boot beans should be present",
            () -> assertDoesNotThrow(() -> applicationContext.getBean("dataSource"), 
                                   "DataSource bean should be available"),
            () -> assertDoesNotThrow(() -> applicationContext.getBean("entityManagerFactory"), 
                                   "EntityManagerFactory should be available")
        );
    }
}
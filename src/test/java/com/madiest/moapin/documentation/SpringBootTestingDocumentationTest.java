package com.madiest.moapin.documentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Specialized tests for Spring Boot testing documentation aspects.
 * Validates that README_TESTING.md provides appropriate guidance for Spring Boot testing patterns.
 * Testing Framework: JUnit 5 (Jupiter) with Spring Boot focus
 */
@DisplayName("Spring Boot Testing Documentation Validation")
class SpringBootTestingDocumentationTest {

    private static final String README_PATH = "src/test/java/com/madiest/moapin/README_TESTING.md";
    private String documentationContent;

    @BeforeEach
    void setUp() throws IOException {
        documentationContent = Files.readString(Paths.get(README_PATH)).toLowerCase();
    }

    @Test
    @DisplayName("Should mention Spring Boot test annotations")
    void shouldMentionSpringBootTestAnnotations() {
        boolean hasSpringBootTestAnnotations = documentationContent.contains("@springboottest") ||
                                             documentationContent.contains("@webmvctest") ||
                                             documentationContent.contains("@datajpatest") ||
                                             documentationContent.contains("@webtestclient") ||
                                             documentationContent.contains("@testcontainers");
        
        assertTrue(hasSpringBootTestAnnotations, 
            "Documentation should mention Spring Boot test annotations");
    }

    @Test
    @DisplayName("Should provide guidance on test slicing")
    void shouldProvideGuidanceOnTestSlicing() {
        boolean hasTestSlicingInfo = documentationContent.contains("test slice") ||
                                   documentationContent.contains("web layer") ||
                                   documentationContent.contains("data layer") ||
                                   documentationContent.contains("repository test") ||
                                   documentationContent.contains("controller test");
        
        assertTrue(hasTestSlicingInfo, 
            "Documentation should provide guidance on Spring Boot test slicing");
    }

    @Test
    @DisplayName("Should mention application context management")
    void shouldMentionApplicationContextManagement() {
        boolean hasContextInfo = documentationContent.contains("application context") ||
                               documentationContent.contains("test context") ||
                               documentationContent.contains("context caching") ||
                               documentationContent.contains("@dirtiescontext");
        
        assertTrue(hasContextInfo, 
            "Documentation should mention application context management in tests");
    }

    @ParameterizedTest
    @ValueSource(strings = {"mock", "mockbean", "spy", "testcontainers", "profile"})
    @DisplayName("Should mention Spring Boot testing concepts")
    void shouldMentionSpringBootTestingConcepts(String concept) {
        // At least some Spring Boot testing concepts should be present
        boolean hasRelevantConcepts = documentationContent.contains("mock") ||
                                    documentationContent.contains("testcontainers") ||
                                    documentationContent.contains("profile") ||
                                    documentationContent.contains("configuration");
        
        assertTrue(hasRelevantConcepts, 
            "Documentation should mention relevant Spring Boot testing concepts");
    }

    @Test
    @DisplayName("Should provide guidance on integration vs unit testing")
    void shouldProvideGuidanceOnIntegrationVsUnitTesting() {
        boolean hasTestingStrategyInfo = documentationContent.contains("unit test") ||
                                       documentationContent.contains("integration test") ||
                                       documentationContent.contains("end-to-end") ||
                                       documentationContent.contains("testing strategy") ||
                                       documentationContent.contains("test pyramid");
        
        assertTrue(hasTestingStrategyInfo, 
            "Documentation should provide guidance on different testing strategies");
    }

    @Test
    @DisplayName("Should mention Gradle test execution")
    void shouldMentionGradleTestExecution() {
        boolean hasGradleInfo = documentationContent.contains("./gradlew test") ||
                              documentationContent.contains("gradle test") ||
                              documentationContent.contains("build.gradle") ||
                              documentationContent.contains("test task");
        
        assertTrue(hasGradleInfo, 
            "Documentation should mention Gradle test execution commands");
    }

    @Test
    @DisplayName("Should provide examples of test configuration")
    void shouldProvideExamplesOfTestConfiguration() {
        boolean hasConfigExamples = documentationContent.contains("application.yml") ||
                                  documentationContent.contains("application.properties") ||
                                  documentationContent.contains("test properties") ||
                                  documentationContent.contains("@testpropertysource") ||
                                  documentationContent.contains("test configuration");
        
        assertTrue(hasConfigExamples, 
            "Documentation should provide examples of test configuration");
    }

    @Test
    @DisplayName("Should mention testing best practices for REST APIs")
    void shouldMentionTestingBestPracticesForRestApis() {
        boolean hasApiTestingInfo = documentationContent.contains("rest") ||
                                  documentationContent.contains("api") ||
                                  documentationContent.contains("controller") ||
                                  documentationContent.contains("endpoint") ||
                                  documentationContent.contains("mockmvc") ||
                                  documentationContent.contains("webtestclient");
        
        assertTrue(hasApiTestingInfo, 
            "Documentation should mention testing practices for REST APIs");
    }
}
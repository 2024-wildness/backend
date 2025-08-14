package com.madiest.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for build.gradle configuration validation Testing Framework: JUnit 5
 * (Jupiter) with Spring Boot Test
 */
@DisplayName("Gradle Build Configuration Tests")
class GradleBuildConfigurationTest {

  private static final String BUILD_GRADLE_PATH = "build.gradle";

  @Nested
  @DisplayName("Build File Structure Tests")
  class BuildFileStructureTests {

    @Test
    @DisplayName("Should have valid build.gradle file present")
    void shouldHaveValidBuildGradleFile() throws IOException {
      Path buildFile = Paths.get(BUILD_GRADLE_PATH);
      assertTrue(Files.exists(buildFile), "build.gradle file should exist");
      assertTrue(Files.isReadable(buildFile), "build.gradle file should be readable");
      assertTrue(Files.size(buildFile) > 0, "build.gradle file should not be empty");
    }

    @Test
    @DisplayName("Should contain required plugin declarations")
    void shouldContainRequiredPlugins() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Required plugins should be present",
          () -> assertTrue(content.contains("id 'java'"), "Java plugin should be declared"),
          () ->
              assertTrue(
                  content.contains("id 'org.springframework.boot'"),
                  "Spring Boot plugin should be declared"),
          () ->
              assertTrue(
                  content.contains("id 'io.spring.dependency-management'"),
                  "Dependency management plugin should be declared"),
          () ->
              assertTrue(
                  content.contains("id 'org.graalvm.buildtools.native'"),
                  "GraalVM native plugin should be declared"),
          () ->
              assertTrue(
                  content.contains("id 'org.asciidoctor.jvm.convert'"),
                  "AsciiDoctor plugin should be declared"));
    }

    @Test
    @DisplayName("Should have proper project metadata")
    void shouldHaveProperProjectMetadata() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Project metadata should be properly configured",
          () ->
              assertTrue(
                  content.contains("group = 'com.madiest'"), "Group should be set to com.madiest"),
          () ->
              assertTrue(
                  content.contains("version = '0.0.1-SNAPSHOT'"),
                  "Version should be set correctly"),
          () ->
              assertTrue(
                  content.contains("JavaLanguageVersion.of(21)"),
                  "Java version should be set to 21"));
    }
  }

  @Nested
  @DisplayName("Dependencies Validation Tests")
  class DependenciesValidationTests {

    @Test
    @DisplayName("Should contain essential Spring Boot starters")
    void shouldContainEssentialSpringBootStarters() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Essential Spring Boot starters should be present",
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-web"), "Web starter should be present"),
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-security"),
                  "Security starter should be present"),
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-data-jpa"),
                  "JPA starter should be present"),
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-data-redis"),
                  "Redis starter should be present"),
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-actuator"),
                  "Actuator starter should be present"));
    }

    @Test
    @DisplayName("Should contain required test dependencies")
    void shouldContainRequiredTestDependencies() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Test dependencies should be properly configured",
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-test"),
                  "Spring Boot test starter should be present"),
          () ->
              assertTrue(
                  content.contains("spring-security-test"),
                  "Spring Security test should be present"),
          () ->
              assertTrue(content.contains("spring-graphql-test"), "GraphQL test should be present"),
          () ->
              assertTrue(
                  content.contains("spring-modulith-starter-test"),
                  "Modulith test should be present"),
          () ->
              assertTrue(
                  content.contains("junit-platform-launcher"),
                  "JUnit platform launcher should be present"));
    }

    @Test
    @DisplayName("Should contain AWS SDK dependencies")
    void shouldContainAwsSdkDependencies() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "AWS SDK dependencies should be present",
          () ->
              assertTrue(
                  content.contains("software.amazon.awssdk:s3"), "AWS S3 SDK should be present"),
          () ->
              assertTrue(
                  content.contains("software.amazon.awssdk:sesv2"),
                  "AWS SES v2 SDK should be present"),
          () ->
              assertTrue(
                  content.contains("software.amazon.awssdk:bom:2.32.5"),
                  "AWS SDK BOM should specify version"));
    }

    @Test
    @DisplayName("Should contain external service dependencies")
    void shouldContainExternalServiceDependencies() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "External service dependencies should be present",
          () ->
              assertTrue(
                  content.contains("com.meilisearch.sdk:meilisearch-java"),
                  "Meilisearch SDK should be present"),
          () ->
              assertTrue(content.contains("io.jsonwebtoken:jjwt-api"), "JWT API should be present"),
          () ->
              assertTrue(
                  content.contains("io.jsonwebtoken:jjwt-impl"),
                  "JWT implementation should be present"),
          () ->
              assertTrue(
                  content.contains("io.jsonwebtoken:jjwt-jackson"),
                  "JWT Jackson should be present"));
    }
  }

  @Nested
  @DisplayName("Configuration Validation Tests")
  class ConfigurationValidationTests {

    @Test
    @DisplayName("Should have proper repository configuration")
    void shouldHaveProperRepositoryConfiguration() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));
      assertTrue(
          content.contains("mavenCentral()"), "Maven Central repository should be configured");
    }

    @Test
    @DisplayName("Should have proper extension variables")
    void shouldHaveProperExtensionVariables() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Extension variables should be properly set",
          () ->
              assertTrue(
                  content.contains("set('snippetsDir', file(\"build/generated-snippets\"))"),
                  "Snippets directory should be configured"),
          () ->
              assertTrue(
                  content.contains("set('springModulithVersion', \"1.4.1\")"),
                  "Spring Modulith version should be set"));
    }

    @Test
    @DisplayName("Should have proper dependency management configuration")
    void shouldHaveProperDependencyManagement() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Dependency management should be properly configured",
          () ->
              assertTrue(
                  content.contains("spring-modulith-bom:${springModulithVersion}"),
                  "Modulith BOM should use version variable"),
          () ->
              assertTrue(
                  content.contains("dependencyManagement"),
                  "Dependency management block should exist"),
          () -> assertTrue(content.contains("imports"), "BOM imports should be configured"));
    }

    @Test
    @DisplayName("Should have proper task configurations")
    void shouldHaveProperTaskConfigurations() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Task configurations should be proper",
          () ->
              assertTrue(content.contains("tasks.named('test')"), "Test task should be configured"),
          () ->
              assertTrue(
                  content.contains("useJUnitPlatform()"), "JUnit platform should be configured"),
          () ->
              assertTrue(
                  content.contains("tasks.named('asciidoctor')"),
                  "AsciiDoctor task should be configured"),
          () ->
              assertTrue(content.contains("dependsOn test"), "AsciiDoctor should depend on test"));
    }
  }

  @Nested
  @DisplayName("Version Consistency Tests")
  class VersionConsistencyTests {

    @Test
    @DisplayName("Should have consistent Spring Boot version")
    void shouldHaveConsistentSpringBootVersion() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));
      assertTrue(content.contains("version '3.5.4'"), "Spring Boot version should be 3.5.4");
    }

    @Test
    @DisplayName("Should have consistent JWT version")
    void shouldHaveConsistentJwtVersion() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      long jwtCount =
          content
              .lines()
              .filter(line -> line.contains("io.jsonwebtoken") && line.contains("0.12.5"))
              .count();

      assertEquals(3, jwtCount, "All JWT dependencies should use version 0.12.5");
    }

    @Test
    @DisplayName("Should have proper dependency scopes")
    void shouldHaveProperDependencyScopes() throws IOException {
      List<String> lines = Files.readAllLines(Paths.get(BUILD_GRADLE_PATH));

      boolean hasImplementationDeps =
          lines.stream().anyMatch(line -> line.trim().startsWith("implementation"));
      boolean hasTestImplementationDeps =
          lines.stream().anyMatch(line -> line.trim().startsWith("testImplementation"));
      boolean hasRuntimeOnlyDeps =
          lines.stream().anyMatch(line -> line.trim().startsWith("runtimeOnly"));
      boolean hasCompileOnlyDeps =
          lines.stream().anyMatch(line -> line.trim().startsWith("compileOnly"));

      assertAll(
          "Dependencies should have proper scopes",
          () -> assertTrue(hasImplementationDeps, "Should have implementation dependencies"),
          () ->
              assertTrue(hasTestImplementationDeps, "Should have test implementation dependencies"),
          () -> assertTrue(hasRuntimeOnlyDeps, "Should have runtime only dependencies"),
          () -> assertTrue(hasCompileOnlyDeps, "Should have compile only dependencies"));
    }
  }

  @Nested
  @DisplayName("Build Script Syntax Tests")
  class BuildScriptSyntaxTests {

    @Test
    @DisplayName("Should have properly formatted plugin block")
    void shouldHaveProperlyFormattedPluginBlock() throws IOException {
      List<String> lines = Files.readAllLines(Paths.get(BUILD_GRADLE_PATH));

      boolean foundPluginsStart = false;
      boolean foundPluginsEnd = false;

      for (String line : lines) {
        if (line.trim().equals("plugins {")) {
          foundPluginsStart = true;
        } else if (foundPluginsStart && line.trim().equals("}")) {
          foundPluginsEnd = true;
          break;
        }
      }

      boolean finalFoundPluginsStart = foundPluginsStart;
      boolean finalFoundPluginsEnd = foundPluginsEnd;
      assertAll(
          "Plugin block should be properly formatted",
          () -> assertTrue(finalFoundPluginsStart, "Plugins block should start with 'plugins {'"),
          () -> assertTrue(finalFoundPluginsEnd, "Plugins block should end with '}'"));
    }

    @Test
    @DisplayName("Should have properly formatted dependencies block")
    void shouldHaveProperlyFormattedDependenciesBlock() throws IOException {
      List<String> lines = Files.readAllLines(Paths.get(BUILD_GRADLE_PATH));

      boolean foundDependenciesStart = false;
      boolean foundDependenciesEnd = false;

      for (String line : lines) {
        if (line.trim().equals("dependencies {")) {
          foundDependenciesStart = true;
        } else if (foundDependenciesStart
            && line.trim().equals("}")
            && !line.contains("dependencyManagement")) {
          foundDependenciesEnd = true;
          break;
        }
      }

      boolean finalFoundDependenciesStart = foundDependenciesStart;
      boolean finalFoundDependenciesEnd = foundDependenciesEnd;
      assertAll(
          "Dependencies block should be properly formatted",
          () ->
              assertTrue(
                  finalFoundDependenciesStart,
                  "Dependencies block should start with 'dependencies {'"),
          () -> assertTrue(finalFoundDependenciesEnd, "Dependencies block should end with '}'"));
    }

    @Test
    @DisplayName("Should not have syntax errors in Gradle script")
    void shouldNotHaveSyntaxErrors() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      // Check for common syntax issues
      assertAll(
          "Should not have common syntax errors",
          () -> assertFalse(content.contains("''"), "Should not have empty string literals"),
          () ->
              assertEquals(
                  content.chars().filter(ch -> ch == '{').count(),
                  content.chars().filter(ch -> ch == '}').count(),
                  "Braces should be balanced"),
          () ->
              assertEquals(
                  content.chars().filter(ch -> ch == '(').count(),
                  content.chars().filter(ch -> ch == ')').count(),
                  "Parentheses should be balanced"));
    }
  }

  @Nested
  @DisplayName("Security and Best Practices Tests")
  class SecurityAndBestPracticesTests {

    @Test
    @DisplayName("Should use specific versions for critical dependencies")
    void shouldUseSpecificVersionsForCriticalDependencies() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Critical dependencies should have specific versions",
          () ->
              assertTrue(
                  content.contains("meilisearch-java:0.12.0"),
                  "Meilisearch should have specific version"),
          () ->
              assertTrue(
                  content.contains("jjwt-api:0.12.5"), "JWT API should have specific version"),
          () ->
              assertTrue(
                  content.contains("spring-modulith-bom:${springModulithVersion}"),
                  "Modulith should use version variable"));
    }

    @Test
    @DisplayName("Should separate development and production dependencies")
    void shouldSeparateDevelopmentAndProductionDependencies() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Development dependencies should be properly scoped",
          () ->
              assertTrue(
                  content.contains(
                      "developmentOnly 'org.springframework.boot:spring-boot-devtools'"),
                  "DevTools should be development only"),
          () ->
              assertTrue(
                  content.contains(
                      "developmentOnly 'org.springframework.boot:spring-boot-docker-compose'"),
                  "Docker Compose should be development only"),
          () ->
              assertTrue(
                  content.contains("testRuntimeOnly 'org.junit.platform:junit-platform-launcher'"),
                  "JUnit launcher should be test runtime only"));
    }

    @Test
    @DisplayName("Should have proper annotation processor configuration")
    void shouldHaveProperAnnotationProcessorConfiguration() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Annotation processors should be properly configured",
          () ->
              assertTrue(
                  content.contains(
                      "annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'"),
                  "Spring Boot configuration processor should be present"),
          () ->
              assertTrue(
                  content.contains("annotationProcessor 'org.projectlombok:lombok'"),
                  "Lombok annotation processor should be present"),
          () ->
              assertTrue(
                  content.contains("compileOnly 'org.projectlombok:lombok'"),
                  "Lombok should be compile only"));
    }
  }

  @Nested
  @DisplayName("Integration Validation Tests")
  class IntegrationValidationTests {

    @Test
    @DisplayName("Should have compatible Spring and Spring Boot versions")
    void shouldHaveCompatibleSpringVersions() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      // Spring Boot 3.5.4 should be compatible with the dependencies
      assertTrue(content.contains("'3.5.4'"), "Spring Boot version should be 3.5.4");
      assertTrue(
          content.contains("JavaLanguageVersion.of(21)"),
          "Should use Java 21 which is compatible with Spring Boot 3.5.4");
    }

    @Test
    @DisplayName("Should have compatible database dependencies")
    void shouldHaveCompatibleDatabaseDependencies() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Database dependencies should be compatible",
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-data-jpa"),
                  "JPA starter should be present"),
          () ->
              assertTrue(
                  content.contains("spring-boot-starter-data-jdbc"),
                  "JDBC starter should be present"),
          () -> assertTrue(content.contains("h2"), "H2 database should be available for testing"),
          () ->
              assertTrue(
                  content.contains("postgresql"), "PostgreSQL should be available for production"));
    }

    @Test
    @DisplayName("Should have proper modular architecture setup")
    void shouldHaveProperModularArchitectureSetup() throws IOException {
      String content = Files.readString(Paths.get(BUILD_GRADLE_PATH));

      assertAll(
          "Modular architecture should be properly configured",
          () ->
              assertTrue(
                  content.contains("spring-modulith-starter-core"),
                  "Modulith core should be present"),
          () ->
              assertTrue(
                  content.contains("spring-modulith-starter-jdbc"),
                  "Modulith JDBC should be present"),
          () ->
              assertTrue(
                  content.contains("spring-modulith-events-api"),
                  "Modulith events API should be present"),
          () ->
              assertTrue(
                  content.contains("spring-modulith-events-amqp"),
                  "Modulith AMQP events should be present"),
          () ->
              assertTrue(
                  content.contains("spring-modulith-starter-test"),
                  "Modulith test support should be present"));
    }
  }
}

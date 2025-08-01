package com.madiest.moapin.documentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests that validate the README_TESTING.md documentation
 * against the actual project structure and test setup.
 * Testing Framework: JUnit 5 (Jupiter) with conditional execution
 */
@DisplayName("Testing Documentation Integration Validation")
class TestingDocumentationIntegrationTest {

    @Test
    @DisplayName("Should validate documentation matches actual project structure")
    void shouldValidateDocumentationMatchesProjectStructure() throws IOException {
        Path testDir = Paths.get("src/test/java");
        assertTrue(Files.exists(testDir), "Test directory should exist as documented");
        
        // Verify there are actual test files in the project
        try (Stream<Path> testFiles = Files.walk(testDir)) {
            long javaTestFileCount = testFiles
                .filter(path -> path.toString().endsWith(".java"))
                .filter(path -> !path.toString().contains("README"))
                .count();
            
            assertTrue(javaTestFileCount > 0, 
                "Project should contain actual Java test files beyond documentation");
        }
        
        // Verify the main source directory exists
        Path mainDir = Paths.get("src/main/java");
        assertTrue(Files.exists(mainDir), "Main source directory should exist");
    }

    @Test
    @DisplayName("Should validate build configuration supports testing")
    @EnabledIf("buildFileExists")
    void shouldValidateBuildConfigurationSupportsTestingWhenBuildFileExists() throws IOException {
        Path gradlePath = Paths.get("build.gradle");
        Path gradleKtsPath = Paths.get("build.gradle.kts");
        
        if (Files.exists(gradlePath)) {
            String buildContent = Files.readString(gradlePath).toLowerCase();
            assertTrue(buildContent.contains("junit") || buildContent.contains("test") || buildContent.contains("spring-boot-starter-test"), 
                "Gradle build file should contain testing dependencies");
        }
        
        if (Files.exists(gradleKtsPath)) {
            String buildContent = Files.readString(gradleKtsPath).toLowerCase();
            assertTrue(buildContent.contains("junit") || buildContent.contains("test") || buildContent.contains("spring-boot-starter-test"), 
                "Gradle Kotlin build file should contain testing dependencies");
        }
    }

    @Test
    @DisplayName("Should validate test package structure follows conventions")
    void shouldValidateTestPackageStructureFollowsConventions() throws IOException {
        Path testJavaDir = Paths.get("src/test/java");
        
        if (Files.exists(testJavaDir)) {
            try (Stream<Path> packageDirs = Files.walk(testJavaDir)) {
                List<Path> javaDirs = packageDirs
                    .filter(Files::isDirectory)
                    .filter(path -> path.toString().contains("com/madiest/moapin"))
                    .toList();
                
                assertFalse(javaDirs.isEmpty(), 
                    "Should follow standard Java package structure in test directory");
            }
        }
    }

    @Test
    @DisplayName("Should validate documentation location follows Maven/Gradle conventions")
    void shouldValidateDocumentationLocationFollowsConventions() {
        Path readmePath = Paths.get("src/test/java/com/madiest/moapin/README_TESTING.md");
        assertTrue(Files.exists(readmePath), 
            "README_TESTING.md should be located in the appropriate test package directory");
        
        // Verify it's in the test source tree, not main source tree
        assertFalse(Paths.get("src/main/java/com/madiest/moapin/README_TESTING.md").toFile().exists(), 
            "Testing documentation should be in test directory, not main source directory");
    }

    @Test
    @DisplayName("Should validate existing test files follow JUnit 5 conventions")
    void shouldValidateExistingTestFilesFollowJUnit5Conventions() throws IOException {
        Path testDir = Paths.get("src/test/java/com/madiest/moapin");
        
        if (Files.exists(testDir)) {
            try (Stream<Path> testFiles = Files.walk(testDir)) {
                List<Path> javaTestFiles = testFiles
                    .filter(path -> path.toString().endsWith("Test.java"))
                    .toList();
                
                if (!javaTestFiles.isEmpty()) {
                    // Check at least one test file for JUnit 5 patterns
                    Path sampleTestFile = javaTestFiles.get(0);
                    String content = Files.readString(sampleTestFile);
                    
                    boolean hasJUnit5Imports = content.contains("org.junit.jupiter") ||
                                             content.contains("import org.junit.jupiter.api.Test");
                    
                    assertTrue(hasJUnit5Imports || content.contains("@Test"), 
                        "Test files should use JUnit 5 (Jupiter) annotations and imports");
                }
            }
        }
    }

    @Test
    @DisplayName("Should validate Spring Boot test setup if Spring Boot is used")
    void shouldValidateSpringBootTestSetupIfSpringBootIsUsed() throws IOException {
        Path testDir = Paths.get("src/test/java");
        
        if (Files.exists(testDir)) {
            try (Stream<Path> testFiles = Files.walk(testDir)) {
                boolean hasSpringBootTests = testFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .anyMatch(path -> {
                        try {
                            String content = Files.readString(path);
                            return content.contains("@SpringBootTest") || 
                                   content.contains("SpringBootTest") ||
                                   content.contains("@WebMvcTest") ||
                                   content.contains("@DataJpaTest");
                        } catch (IOException e) {
                            return false;
                        }
                    });
                
                if (hasSpringBootTests) {
                    // If Spring Boot tests exist, the README should mention Spring Boot testing
                    Path readmePath = Paths.get("src/test/java/com/madiest/moapin/README_TESTING.md");
                    if (Files.exists(readmePath)) {
                        String readmeContent = Files.readString(readmePath).toLowerCase();
                        assertTrue(readmeContent.contains("spring") || readmeContent.contains("boot") || readmeContent.contains("context"),
                            "README should mention Spring Boot testing when Spring Boot tests are present");
                    }
                }
            }
        }
    }

    private boolean buildFileExists() {
        return Files.exists(Paths.get("build.gradle")) || 
               Files.exists(Paths.get("build.gradle.kts")) ||
               Files.exists(Paths.get("pom.xml"));
    }
}
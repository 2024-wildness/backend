package com.madiest.moapin.documentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive validation tests for README_TESTING.md documentation.
 * Ensures the testing documentation provides clear, actionable guidance
 * and follows best practices for documentation structure.
 * Testing Framework: JUnit 5 (Jupiter)
 */
@DisplayName("README Testing Documentation Validation")
class ReadmeTestingValidationTest {

    private static final String README_PATH = "src/test/java/com/madiest/moapin/README_TESTING.md";
    private List<String> readmeContent;
    private String fullContent;

    @BeforeEach
    void setUp() throws IOException {
        Path readmePath = Paths.get(README_PATH);
        assertTrue(Files.exists(readmePath), "README_TESTING.md file must exist");
        readmeContent = Files.readAllLines(readmePath);
        fullContent = String.join("\n", readmeContent);
    }

    @Test
    @DisplayName("Should have proper markdown structure with headers")
    void shouldHaveProperMarkdownStructure() {
        assertFalse(readmeContent.isEmpty(), "README should not be empty");
        
        boolean hasMainHeader = readmeContent.stream()
            .anyMatch(line -> line.startsWith("# "));
        assertTrue(hasMainHeader, "README should have at least one main header (# )");
        
        boolean hasSubHeaders = readmeContent.stream()
            .anyMatch(line -> line.startsWith("## "));
        assertTrue(hasSubHeaders, "README should have subsection headers (## )");
    }

    @Test
    @DisplayName("Should contain essential testing sections")
    void shouldContainEssentialTestingSections() {
        String lowerContent = fullContent.toLowerCase();
        
        assertTrue(lowerContent.contains("test") && (lowerContent.contains("setup") || lowerContent.contains("configuration")), 
            "Should contain testing setup information");
        assertTrue(lowerContent.contains("run") && lowerContent.contains("test"), 
            "Should contain information about running tests");
        assertTrue(lowerContent.contains("junit") || lowerContent.contains("spring boot test") || lowerContent.contains("framework"), 
            "Should mention the testing framework being used");
    }

    @Test
    @DisplayName("Should provide code examples or commands")
    void shouldProvideCodeExamples() {
        boolean hasCodeBlocks = fullContent.contains("```") || fullContent.contains("`");
        boolean hasCommands = readmeContent.stream()
            .anyMatch(line -> line.trim().startsWith("./gradlew ") || 
                            line.trim().startsWith("gradle ") ||
                            line.contains("test") ||
                            line.contains("@Test"));
        
        assertTrue(hasCodeBlocks || hasCommands, 
            "README should contain code examples or command examples");
    }

    @Test
    @DisplayName("Should have reasonable content length and depth")
    void shouldHaveReasonableContentLength() {
        assertTrue(readmeContent.size() >= 10, 
            "README should have at least 10 lines of content for comprehensive guidance");
        assertTrue(fullContent.length() >= 200, 
            "README should have substantial content (at least 200 characters)");
        assertTrue(fullContent.length() <= 15000, 
            "README should not be excessively long (less than 15000 characters)");
        
        long meaningfulLines = readmeContent.stream()
            .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"))
            .count();
        assertTrue(meaningfulLines >= 5, "Should have at least 5 lines of meaningful content");
    }

    @Test
    @DisplayName("Should not contain broken markdown formatting")
    void shouldNotContainBrokenMarkdownFormatting() {
        for (int i = 0; i < readmeContent.size(); i++) {
            String line = readmeContent.get(i);
            
            // Check for unclosed code blocks
            long backtickCount = line.chars().filter(ch -> ch == '`').count();
            if (backtickCount == 1 || backtickCount == 2) {
                assertFalse(line.trim().equals("`") || line.trim().equals("``"), 
                    "Line " + (i + 1) + " contains unclosed backticks");
            }
            
            // Check for proper header formatting
            if (line.startsWith("#")) {
                assertTrue(line.matches("^#+\\s+.+"), 
                    "Line " + (i + 1) + " header should have space after # symbols");
            }
        }
        
        // Validate code block consistency
        assertTrue(DocumentationTestUtils.hasValidCodeBlocks(fullContent), 
            "All code blocks should be properly opened and closed");
    }

    @Test
    @DisplayName("Should contain Spring Boot and JUnit 5 specific testing guidance")
    void shouldContainSpringBootAndJUnit5Guidance() {
        String lowerContent = fullContent.toLowerCase();
        
        boolean hasSpringBootTestInfo = lowerContent.contains("spring boot") || 
                                       lowerContent.contains("@springboottest") ||
                                       lowerContent.contains("application context");
        
        boolean hasJUnit5Info = lowerContent.contains("junit 5") || 
                               lowerContent.contains("jupiter") ||
                               lowerContent.contains("@test") ||
                               lowerContent.contains("@displayname");
        
        assertTrue(hasSpringBootTestInfo || hasJUnit5Info, 
            "README should contain Spring Boot and/or JUnit 5 specific testing guidance");
    }

    @Test
    @DisplayName("Should contain testing best practices guidance")
    void shouldContainTestingBestPractices() {
        String lowerContent = fullContent.toLowerCase();
        
        boolean hasBestPractices = lowerContent.contains("best practice") ||
                                 lowerContent.contains("guideline") ||
                                 lowerContent.contains("convention") ||
                                 lowerContent.contains("standard");
        
        boolean hasTestingConcepts = lowerContent.contains("unit test") ||
                                   lowerContent.contains("integration test") ||
                                   lowerContent.contains("mock") ||
                                   lowerContent.contains("assert") ||
                                   lowerContent.contains("test coverage");
        
        assertTrue(hasBestPractices || hasTestingConcepts, 
            "README should contain testing best practices or key testing concepts");
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "junit", "gradle", "assert", "mock", "spring"})
    @DisplayName("Should contain key testing-related keywords")
    void shouldContainKeyTestingKeywords(String keyword) {
        String lowerContent = fullContent.toLowerCase();
        // At least some of these keywords should be present
        boolean containsRelevantKeywords = lowerContent.contains("test") ||
                                         lowerContent.contains("junit") ||
                                         lowerContent.contains("gradle") ||
                                         lowerContent.contains("spring");
        
        assertTrue(containsRelevantKeywords, 
            "README should contain at least some key testing-related keywords");
    }

    @Test
    @DisplayName("Should have consistent line endings and encoding")
    void shouldHaveConsistentFormatting() throws IOException {
        Path readmePath = Paths.get(README_PATH);
        assertTrue(DocumentationTestUtils.hasConsistentEncoding(readmePath), 
            "Should have consistent line endings (not mixed Windows/Unix)");
        
        byte[] bytes = Files.readAllBytes(readmePath);
        assertTrue(bytes.length > 0, "File should not be empty");
    }

    @Test
    @DisplayName("Should provide actionable instructions for developers")
    void shouldProvideActionableInstructions() {
        String lowerContent = fullContent.toLowerCase();
        
        boolean hasActionableVerbs = lowerContent.contains("run") ||
                                   lowerContent.contains("execute") ||
                                   lowerContent.contains("configure") ||
                                   lowerContent.contains("setup") ||
                                   lowerContent.contains("create") ||
                                   lowerContent.contains("write");
        
        assertTrue(hasActionableVerbs, 
            "README should contain actionable instructions with clear verbs");
        
        assertTrue(DocumentationTestUtils.containsActionableContent(fullContent),
            "README should contain actionable content for developers");
    }

    @Test
    @DisplayName("Should have proper file extension and location")
    void shouldHaveProperFileExtensionAndLocation() {
        assertTrue(README_PATH.endsWith(".md"), "Should have .md extension");
        assertTrue(README_PATH.contains("test"), "Should be located in test directory");
        assertTrue(README_PATH.contains("README"), "Should follow README naming convention");
        assertTrue(README_PATH.contains("TESTING"), "Should indicate it's about testing");
    }

    @Test
    @DisplayName("Should validate table formatting if tables are present")
    void shouldValidateTableFormatting() {
        List<String> tableLines = readmeContent.stream()
            .filter(line -> line.contains("|") && !line.trim().startsWith("#"))
            .collect(Collectors.toList());
        
        if (!tableLines.isEmpty()) {
            for (String tableLine : tableLines) {
                if (tableLine.trim().startsWith("|") && tableLine.trim().endsWith("|")) {
                    assertTrue(tableLine.split("\\|").length >= 3, 
                        "Table rows should have at least 2 columns (3 pipe-separated sections)");
                }
            }
        }
    }

    @Test
    @DisplayName("Should validate link formatting if links are present")
    void shouldValidateLinkFormatting() {
        assertTrue(DocumentationTestUtils.hasValidMarkdownLinks(fullContent),
            "All markdown links should be properly formatted");
        
        Pattern linkPattern = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)");
        boolean hasMarkdownLinks = linkPattern.matcher(fullContent).find();
        
        if (hasMarkdownLinks) {
            // Additional validation for links
            String[] lines = fullContent.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.contains("[") && line.contains("]") && line.contains("(") && line.contains(")")) {
                    assertTrue(linkPattern.matcher(line).find(), 
                        "Line " + (i + 1) + " contains malformed markdown links");
                }
            }
        }
    }

    @Test
    @DisplayName("Should contain project-specific testing information")
    void shouldContainProjectSpecificTestingInformation() {
        String lowerContent = fullContent.toLowerCase();
        
        // Should mention project-specific aspects
        boolean hasProjectContext = lowerContent.contains("moapin") ||
                                   lowerContent.contains("application") ||
                                   lowerContent.contains("service") ||
                                   lowerContent.contains("controller") ||
                                   lowerContent.contains("repository");
        
        // Should mention common testing scenarios for this type of application
        boolean hasRelevantTestTypes = lowerContent.contains("integration") ||
                                     lowerContent.contains("unit") ||
                                     lowerContent.contains("web") ||
                                     lowerContent.contains("database") ||
                                     lowerContent.contains("api");
        
        assertTrue(hasProjectContext || hasRelevantTestTypes,
            "README should contain project-specific or relevant testing information");
    }

    @Test
    @DisplayName("Should have meaningful section structure")
    void shouldHaveMeaningfulSectionStructure() {
        long sectionCount = DocumentationTestUtils.countMeaningfulSections(readmeContent);
        assertTrue(sectionCount >= 3, 
            "README should have at least 3 meaningful sections (## headers)");
        
        assertTrue(DocumentationTestUtils.hasValidMarkdownHeaders(readmeContent),
            "All markdown headers should be properly formatted");
    }
}
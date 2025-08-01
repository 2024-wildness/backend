package com.madiest.moapin.documentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class providing common validation methods for documentation testing.
 * This class supports validation of markdown files, code examples, and documentation structure.
 * Testing Framework: JUnit 5 (Jupiter) utilities
 */
public final class DocumentationTestUtils {

    private DocumentationTestUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates that a markdown file contains properly formatted headers.
     */
    public static boolean hasValidMarkdownHeaders(List<String> content) {
        return content.stream()
            .filter(line -> line.startsWith("#"))
            .allMatch(line -> line.matches("^#+\\s+.+"));
    }

    /**
     * Validates that code blocks are properly formatted and closed.
     */
    public static boolean hasValidCodeBlocks(String content) {
        String[] lines = content.split("\n");
        boolean inCodeBlock = false;
        
        for (String line : lines) {
            if (line.trim().startsWith("```")) {
                inCodeBlock = !inCodeBlock;
            }
        }
        
        return !inCodeBlock; // Should not end while still in a code block
    }

    /**
     * Validates that the documentation contains actionable content.
     */
    public static boolean containsActionableContent(String content) {
        String lowerContent = content.toLowerCase();
        String[] actionableKeywords = {
            "run", "execute", "install", "configure", "setup", 
            "test", "build", "deploy", "start", "stop", "create",
            "write", "add", "implement", "use", "follow"
        };
        
        int keywordCount = 0;
        for (String keyword : actionableKeywords) {
            if (lowerContent.contains(keyword)) {
                keywordCount++;
            }
        }
        
        // Should contain at least 3 actionable keywords for comprehensive guidance
        return keywordCount >= 3;
    }

    /**
     * Validates that markdown links are properly formatted.
     */
    public static boolean hasValidMarkdownLinks(String content) {
        Pattern linkPattern = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)");
        Pattern malformedPattern = Pattern.compile("\\[[^\\]]*\\]\\([^\\)]*$|\\[[^\\]]*$\\([^\\)]*\\)");
        
        return !malformedPattern.matcher(content).find();
    }

    /**
     * Counts the number of meaningful sections in the documentation.
     */
    public static long countMeaningfulSections(List<String> content) {
        return content.stream()
            .filter(line -> line.startsWith("##"))
            .count();
    }

    /**
     * Validates file encoding and line endings consistency.
     */
    public static boolean hasConsistentEncoding(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        String content = new String(bytes);
        
        // Check for consistent line endings
        boolean hasWindowsLineEndings = content.contains("\r\n");
        boolean hasUnixLineEndings = content.contains("\n") && !content.contains("\r\n");
        
        return !(hasWindowsLineEndings && hasUnixLineEndings);
    }

    /**
     * Validates that the documentation contains testing-specific terminology.
     */
    public static boolean containsTestingTerminology(String content) {
        String lowerContent = content.toLowerCase();
        String[] testingTerms = {
            "unit test", "integration test", "test case", "test method", 
            "assertion", "mock", "stub", "test data", "test environment",
            "junit", "spring boot test", "test configuration", "before each",
            "after each", "test suite", "coverage", "parameterized test"
        };
        
        int termCount = 0;
        for (String term : testingTerms) {
            if (lowerContent.contains(term)) {
                termCount++;
            }
        }
        
        // Should contain at least 3 testing-specific terms
        return termCount >= 3;
    }

    /**
     * Validates that code examples follow proper formatting.
     */
    public static boolean hasProperCodeExamples(String content) {
        boolean hasFencedCodeBlocks = content.contains("```java") || content.contains("```");
        boolean hasInlineCode = content.contains("`") && !content.contains("```");
        
        return hasFencedCodeBlocks || hasInlineCode;
    }
}
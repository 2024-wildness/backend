# GitHub Template Tests

This directory contains comprehensive unit tests for GitHub issue templates.

## Testing Framework

- **Framework**: Jest (JavaScript testing framework)
- **YAML Parser**: js-yaml for parsing front matter
- **Test Focus**: Template validation, structure, and GitHub compatibility

## Test Coverage

### Refactor Template Tests
- File existence and UTF-8 encoding
- YAML front matter validation
- Template body content validation
- Korean language consistency
- GitHub integration compatibility
- Edge cases and error handling
- Metadata and branding consistency

### Cross-Template Tests
- Consistency across all issue templates
- Structural validation
- Encoding verification
- Naming convention compliance

## Running Tests

```bash
# Run all tests
npm test

# Run template-specific tests
npm run test:templates

# Run with coverage
npm test -- --coverage
```

## Test Philosophy

These tests follow a comprehensive validation approach:
1. **Structure validation**: Ensuring proper YAML and Markdown format
2. **Content validation**: Verifying exact text and Korean language usage
3. **GitHub compatibility**: Testing integration with GitHub's issue system
4. **Error resilience**: Handling malformed content gracefully
5. **Encoding safety**: Ensuring proper Unicode/UTF-8 handling
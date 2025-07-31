const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');

describe('GitHub Issue Template Validation', () => {
  const templatePath = path.join(__dirname, '../ISSUE_TEMPLATE');
  let templateFiles = [];

  beforeAll(() => {
    // Discover all template files
    if (fs.existsSync(templatePath)) {
      templateFiles = fs.readdirSync(templatePath)
        .filter(file => file.endsWith('.md') || file.endsWith('.yml') || file.endsWith('.yaml'))
        .map(file => path.join(templatePath, file));
    }
    
    // Also check for templates in root .github directory
    const rootTemplates = fs.readdirSync(path.join(__dirname, '..'))
      .filter(file => file.includes('template') && (file.endsWith('.md') || file.endsWith('.yml') || file.endsWith('.yaml')))
      .map(file => path.join(__dirname, '..', file));
    
    templateFiles = [...templateFiles, ...rootTemplates];
  });

  describe('Refactor Template Validation', () => {
    const refactorTemplatePath = path.join(__dirname, '../ISSUE_TEMPLATE/♻️-refactor.md');
    let templateContent;
    let frontMatter;
    let bodyContent;

    beforeEach(() => {
      if (fs.existsSync(refactorTemplatePath)) {
        templateContent = fs.readFileSync(refactorTemplatePath, 'utf8');
        const parts = templateContent.split('---');
        if (parts.length >= 3) {
          try {
            frontMatter = yaml.load(parts[1]);
            bodyContent = parts.slice(2).join('---').trim();
          } catch (e) {
            // Handle YAML parsing errors gracefully
            frontMatter = null;
            bodyContent = '';
          }
        }
      }
    });

    describe('File Existence and Structure', () => {
      test('refactor template file should exist', () => {
        expect(fs.existsSync(refactorTemplatePath)).toBe(true);
      });

      test('should be readable as UTF-8', () => {
        expect(() => fs.readFileSync(refactorTemplatePath, 'utf8')).not.toThrow();
        expect(templateContent).toBeDefined();
        expect(typeof templateContent).toBe('string');
        expect(templateContent.length).toBeGreaterThan(0);
      });

      test('should have proper YAML front matter structure', () => {
        expect(templateContent).toMatch(/^---\s*\n[\s\S]*?\n---/m);
        const yamlMatch = templateContent.match(/^---\s*\n([\s\S]*?)\n---/m);
        expect(yamlMatch).toBeTruthy();
        expect(yamlMatch[1]).toBeTruthy();
      });

      test('should have exactly 3 sections (empty, yaml, body)', () => {
        const parts = templateContent.split('---');
        expect(parts.length).toBe(3);
        expect(parts[0]).toBe(''); // Before first ---
        expect(parts[1].trim().length).toBeGreaterThan(0); // YAML content
        expect(parts[2].trim().length).toBeGreaterThan(0); // Body content
      });
    });

    describe('Front Matter Validation', () => {
      test('should have valid YAML front matter', () => {
        expect(frontMatter).toBeTruthy();
        expect(typeof frontMatter).toBe('object');
        expect(frontMatter).not.toBeNull();
      });

      test('should have required name field with exact emoji and text', () => {
        expect(frontMatter).toHaveProperty('name');
        expect(frontMatter.name).toBe('♻️ Refactor');
        expect(typeof frontMatter.name).toBe('string');
        expect(frontMatter.name.length).toBeGreaterThan(0);
        expect(frontMatter.name).toMatch(/♻️/);
        expect(frontMatter.name).toMatch(/Refactor/);
      });

      test('should have exact Korean about description', () => {
        expect(frontMatter).toHaveProperty('about');
        expect(frontMatter.about).toBe('코드를 개선시키거나 구조를 변경합니다.');
        expect(typeof frontMatter.about).toBe('string');
        expect(frontMatter.about.length).toBeGreaterThan(10);
        expect(frontMatter.about).toMatch(/[가-힣]/);
        expect(frontMatter.about).toMatch(/코드/);
        expect(frontMatter.about).toMatch(/개선/);
      });

      test('should have exact title prefix format', () => {
        expect(frontMatter).toHaveProperty('title');
        expect(frontMatter.title).toBe('[refactor] ');
        expect(frontMatter.title).toMatch(/^\[refactor\]\s$/);
        expect(frontMatter.title.endsWith(' ')).toBe(true);
      });

      test('should have empty labels field', () => {
        expect(frontMatter).toHaveProperty('labels');
        expect(frontMatter.labels).toBe('');
        expect(typeof frontMatter.labels).toBe('string');
      });

      test('should have empty assignees field', () => {
        expect(frontMatter).toHaveProperty('assignees');
        expect(frontMatter.assignees).toBe('');
        expect(typeof frontMatter.assignees).toBe('string');
      });

      test('should have exactly 5 expected fields and no extras', () => {
        const expectedFields = ['name', 'about', 'title', 'labels', 'assignees'];
        const actualFields = Object.keys(frontMatter || {});
        expect(actualFields.sort()).toEqual(expectedFields.sort());
        expect(actualFields.length).toBe(5);
      });

      test('should have all required fields with non-empty values', () => {
        const requiredFields = ['name', 'about', 'title'];
        requiredFields.forEach(field => {
          expect(frontMatter).toHaveProperty(field);
          expect(frontMatter[field]).toBeTruthy();
          expect(frontMatter[field].trim().length).toBeGreaterThan(0);
        });
      });
    });

    describe('Template Body Content Validation', () => {
      test('should contain exact work description section header', () => {
        expect(bodyContent).toMatch(/## 작업 내용 설명/);
        expect(bodyContent).toContain('## 작업 내용 설명');
      });

      test('should contain exact checklist section header', () => {
        expect(bodyContent).toMatch(/## 체크리스트/);
        expect(bodyContent).toContain('## 체크리스트');
      });

      test('should have exact instructional comment for work description', () => {
        expect(bodyContent).toContain('<!-- 해당 브랜치에서 작업할 내용을 간단하게 작성해주세요 -->');
        expect(bodyContent).toMatch(/해당 브랜치에서 작업할 내용을 간단하게 작성해주세요/);
      });

      test('should have exact instructional comment for checklist', () => {
        expect(bodyContent).toContain('<!-- "중요한 순서" 대로 작업 리스트를 작성해주세요 -->');
        expect(bodyContent).toMatch(/중요한 순서.*대로 작업 리스트를 작성해주세요/);
      });

      test('should contain properly formatted empty checklist item', () => {
        expect(bodyContent).toMatch(/- \[ \]/);
        expect(bodyContent).toContain('- [ ]');
        expect(bodyContent).not.toMatch(/- \[\]/); // Should have space
      });

      test('should have exactly 2 level-2 markdown headers', () => {
        const headers = bodyContent.match(/^##\s+.+$/gm);
        expect(headers).toBeTruthy();
        expect(headers.length).toBe(2);
        
        headers.forEach(header => {
          expect(header).toMatch(/^##\s+\S/);
        });
      });

      test('should not contain placeholder text or incomplete elements', () => {
        expect(bodyContent.toLowerCase()).not.toMatch(/\btodo\b|\bfixme\b|\bxxx\b|\bplaceholder\b/);
        expect(bodyContent).not.toMatch(/\[\]/); // Should have spaces in checkboxes
        expect(bodyContent).not.toMatch(/\[TODO\]|\[FIXME\]/);
      });

      test('should have sections in correct order', () => {
        const workDescIndex = bodyContent.indexOf('## 작업 내용 설명');
        const checklistIndex = bodyContent.indexOf('## 체크리스트');
        expect(workDescIndex).toBeGreaterThan(-1);
        expect(checklistIndex).toBeGreaterThan(-1);
        expect(workDescIndex).toBeLessThan(checklistIndex);
      });

      test('should have proper line structure and spacing', () => {
        const lines = bodyContent.split('\n');
        expect(lines.length).toBeGreaterThan(5);
        
        // Should have empty lines for spacing
        const hasEmptyLines = lines.some(line => line.trim() === '');
        expect(hasEmptyLines).toBe(true);
      });
    });

    describe('Content Quality and Language Consistency', () => {
      test('should use consistent Korean language throughout template', () => {
        expect(frontMatter.about).toMatch(/[가-힣]/);
        expect(bodyContent).toMatch(/작업 내용 설명/);
        expect(bodyContent).toMatch(/체크리스트/);
        expect(bodyContent).toMatch(/해당 브랜치에서 작업할 내용/);
        expect(bodyContent).toMatch(/중요한 순서/);
      });

      test('should have meaningful instructional comments', () => {
        const comments = bodyContent.match(/<!--[\s\S]*?-->/g);
        expect(comments).toBeTruthy();
        expect(comments.length).toBe(2);
        
        comments.forEach(comment => {
          expect(comment.length).toBeGreaterThan(15); // Substantial comments
          expect(comment).toMatch(/<!--\s+.+\s+-->/s);
          expect(comment).toMatch(/[가-힣]/); // Contains Korean
        });
      });

      test('should provide clear guidance structure for users', () => {
        const sections = bodyContent.match(/^##\s+.+$/gm);
        expect(sections).toBeTruthy();
        expect(sections.length).toBe(2);
        expect(sections[0]).toContain('작업 내용 설명');
        expect(sections[1]).toContain('체크리스트');
      });

      test('should have proper file formatting and encoding', () => {
        expect(templateContent).not.toMatch(/\r\n/); // Unix line endings only
        expect(templateContent.endsWith('\n')).toBe(true); // File ends with newline
        expect(templateContent.startsWith('---\n')).toBe(true); // Proper YAML start
        
        // Check for reasonable spacing between major sections
        const lines = bodyContent.split('\n');
        const emptyLineCount = lines.filter(line => line.trim() === '').length;
        expect(emptyLineCount).toBeGreaterThan(0);
      });
    });

    describe('Edge Cases and Error Handling', () => {
      test('should handle YAML parsing errors appropriately', () => {
        const invalidYaml = templateContent.replace('name: "♻️ Refactor"', 'name: [invalid: yaml}');
        expect(() => {
          const parts = invalidYaml.split('---');
          yaml.load(parts[1]);
        }).toThrow();
      });

      test('should handle content manipulation gracefully', () => {
        const contentWithoutSections = '---\nname: test\nabout: test\ntitle: test\nlabels: ""\nassignees: ""\n---\n\nJust content';
        const parts = contentWithoutSections.split('---');
        const body = parts.slice(2).join('---').trim();
        expect(body).toBe('Just content');
        expect(parts.length).toBe(3);
      });

      test('should properly handle Unicode characters in Korean text', () => {
        expect(frontMatter.about).toMatch(/[\u1100-\u11FF\u3130-\u318F\uAC00-\uD7AF]/);
        expect(bodyContent).toMatch(/[\u1100-\u11FF\u3130-\u318F\uAC00-\uD7AF]/);
        
        // Verify UTF-8 encoding integrity
        const aboutBytes = Buffer.from(frontMatter.about, 'utf8');
        expect(aboutBytes.toString('utf8')).toBe(frontMatter.about);
        
        const bodyBytes = Buffer.from(bodyContent, 'utf8');
        expect(bodyBytes.toString('utf8')).toBe(bodyContent);
      });

      test('should handle whitespace and empty content validation', () => {
        expect(bodyContent.trim().length).toBeGreaterThan(50);
        expect(frontMatter.name.trim().length).toBeGreaterThan(5);
        expect(frontMatter.about.trim().length).toBeGreaterThan(15);
        expect(frontMatter.title.trim()).toBe('[refactor]');
      });

      test('should validate content structure resilience', () => {
        // Test that content can be parsed even with slight modifications
        const modifiedContent = templateContent.replace(/\n\n/g, '\n');
        const parts = modifiedContent.split('---');
        expect(parts.length).toBe(3);
        expect(() => yaml.load(parts[1])).not.toThrow();
      });
    });

    describe('GitHub Integration and Compatibility', () => {
      test('should have proper GitHub issue template format', () => {
        expect(templateContent.startsWith('---')).toBe(true);
        expect(templateContent).toMatch(/^---\n[\s\S]*?\n---\n[\s\S]*$/);
        
        // Should have the right structure for GitHub to recognize
        const frontMatterSection = templateContent.match(/^---([\s\S]*?)---/m);
        expect(frontMatterSection).toBeTruthy();
      });

      test('should have valid GitHub-compatible checkbox syntax', () => {
        const checkboxes = bodyContent.match(/^-\s+\[\s*\]\s*$/gm);
        expect(checkboxes).toBeTruthy();
        expect(checkboxes.length).toBe(1);
        
        // Verify exact checkbox format
        expect(bodyContent).toContain('- [ ]');
        expect(bodyContent).not.toContain('- []');
        expect(bodyContent).not.toContain('-[ ]');
      });

      test('should have proper HTML comment syntax for GitHub', () => {
        const htmlComments = bodyContent.match(/<!--[\s\S]*?-->/g);
        expect(htmlComments).toBeTruthy();
        expect(htmlComments.length).toBe(2);
        
        htmlComments.forEach(comment => {
          expect(comment.startsWith('<!--')).toBe(true);
          expect(comment.endsWith('-->')).toBe(true);
          expect(comment).toMatch(/<!--\s+.+\s+-->/s);
        });
      });

      test('should use appropriate refactor-specific terminology', () => {
        expect(frontMatter.name).toMatch(/refactor/i);
        expect(frontMatter.title).toMatch(/refactor/i);
        expect(frontMatter.about).toMatch(/개선|구조|변경/);
      });

      test('should have GitHub-compatible metadata fields', () => {
        // Verify all GitHub issue template fields are present and properly formatted
        expect(typeof frontMatter.name).toBe('string');
        expect(typeof frontMatter.about).toBe('string');
        expect(typeof frontMatter.title).toBe('string');
        expect(typeof frontMatter.labels).toBe('string');
        expect(typeof frontMatter.assignees).toBe('string');
      });
    });
  });

  describe('Cross-Template Consistency Validation', () => {
    test('all issue templates should exist and be accessible', () => {
      const expectedTemplates = [
        '♻️-refactor.md',
        '✨-feature.md', 
        '🐛-bug.md',
        '🛠️-setting.md'
      ];
      
      expectedTemplates.forEach(template => {
        const templatePath = path.join(__dirname, '../ISSUE_TEMPLATE', template);
        expect(fs.existsSync(templatePath)).toBe(true);
        expect(() => fs.readFileSync(templatePath, 'utf8')).not.toThrow();
        
        const content = fs.readFileSync(templatePath, 'utf8');
        expect(content.length).toBeGreaterThan(100);
      });
    });

    test('all templates should have valid and parseable YAML front matter', () => {
      templateFiles.forEach(templateFile => {
        const content = fs.readFileSync(templateFile, 'utf8');
        if (content.startsWith('---')) {
          const parts = content.split('---');
          expect(parts.length).toBeGreaterThanOrEqual(3);
          
          if (parts.length >= 3) {
            expect(() => yaml.load(parts[1])).not.toThrow();
            const frontMatter = yaml.load(parts[1]);
            expect(frontMatter).toBeTruthy();
            expect(typeof frontMatter).toBe('object');
          }
        }
      });
    });

    test('all templates should have consistent structural elements', () => {
      templateFiles.forEach(templateFile => {
        const content = fs.readFileSync(templateFile, 'utf8').trim();
        expect(content.length).toBeGreaterThan(100); // Reasonable minimum
        expect(content.startsWith('---')).toBe(true);
        expect(content).toMatch(/---[\s\S]*?---[\s\S]*/);
      });
    });

    test('all templates should maintain proper UTF-8 encoding', () => {
      templateFiles.forEach(templateFile => {
        const content = fs.readFileSync(templateFile, 'utf8');
        const reconstructed = Buffer.from(content, 'utf8').toString('utf8');
        expect(reconstructed).toBe(content);
        
        // Should not have encoding artifacts
        expect(content).not.toMatch(/\uFFFD/); // Replacement character
      });
    });

    test('all templates should follow consistent naming conventions', () => {
      const expectedPatterns = [
        /♻️.*refactor/i,
        /✨.*feature/i,
        /🐛.*bug/i,
        /🛠️.*setting/i
      ];
      
      templateFiles.forEach((templateFile, index) => {
        if (index < expectedPatterns.length) {
          const filename = path.basename(templateFile);
          expect(filename).toMatch(expectedPatterns[index]);
        }
      });
    });
  });

  describe('Template Metadata and Branding', () => {
    test('should have distinctive emoji for visual identification', () => {
      expect(frontMatter.name).toMatch(/♻️/);
      expect(frontMatter.name).toContain('♻️ Refactor');
    });

    test('should have contextually appropriate Korean descriptions', () => {
      expect(frontMatter.about).toMatch(/개선|구조|변경/);
      expect(frontMatter.about.length).toBeGreaterThan(15);
      expect(frontMatter.about).toBe('코드를 개선시키거나 구조를 변경합니다.');
    });

    test('should maintain consistent default field values', () => {
      expect(frontMatter.labels).toBe('');
      expect(frontMatter.assignees).toBe('');
      expect(typeof frontMatter.labels).toBe('string');
      expect(typeof frontMatter.assignees).toBe('string');
    });

    test('should match established filename conventions', () => {
      const filename = path.basename(refactorTemplatePath);
      expect(filename).toBe('♻️-refactor.md');
      expect(filename).toMatch(/♻️.*refactor.*\.md$/);
    });

    test('should maintain brand consistency across template elements', () => {
      // Check that refactor theme is consistent
      expect(frontMatter.name.toLowerCase()).toMatch(/refactor/);
      expect(frontMatter.title.toLowerCase()).toMatch(/refactor/);
      expect(frontMatter.about).toMatch(/개선.*구조.*변경/);
    });
  });
});
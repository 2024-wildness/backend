# Task Master AI - Agent Integration Guide

## Essential Commands

### Core Workflow Commands

```bash
# Project Setup
task-master init                                    # Initialize Task Master in current project
task-master parse-prd .taskmaster/docs/prd.txt      # Generate tasks from PRD document
task-master models --setup                        # Configure AI models interactively

# Daily Development Workflow
task-master list                                   # Show all tasks with status
task-master next                                   # Get next available task to work on
task-master show <id>                             # View detailed task information (e.g., task-master show 1.2)
task-master set-status --id=<id> --status=done    # Mark task complete

# Task Management
task-master add-task --prompt="description" --research        # Add new task with AI assistance
task-master expand --id=<id> --research --force              # Break task into subtasks
task-master update-task --id=<id> --prompt="changes"         # Update specific task
task-master update --from=<id> --prompt="changes"            # Update multiple tasks from ID onwards
task-master update-subtask --id=<id> --prompt="notes"        # Add implementation notes to subtask

# Analysis & Planning
task-master analyze-complexity --research          # Analyze task complexity
task-master complexity-report                      # View complexity analysis
task-master expand --all --research               # Expand all eligible tasks

# Dependencies & Organization
task-master add-dependency --id=<id> --depends-on=<id>       # Add task dependency
task-master move --from=<id> --to=<id>                       # Reorganize task hierarchy
task-master validate-dependencies                            # Check for dependency issues
task-master generate                                         # Update task markdown files (usually auto-called)
```

## Key Files & Project Structure

### Core Files

- `.taskmaster/tasks/tasks.json` - Main task data file (auto-managed)
- `.taskmaster/config.json` - AI model configuration (use `task-master models` to modify)
- `.taskmaster/docs/prd.txt` - Product Requirements Document for parsing
- `.taskmaster/tasks/*.txt` - Individual task files (auto-generated from tasks.json)
- `.env` - API keys for CLI usage

### Claude Code Integration Files

- `CLAUDE.md` - Auto-loaded context for Claude Code (this file)
- `.claude/settings.json` - Claude Code tool allowlist and preferences
- `.claude/commands/` - Custom slash commands for repeated workflows
- `.mcp.json` - MCP server configuration (project-specific)

### Directory Structure

```
project/
├── .taskmaster/
│   ├── tasks/              # Task files directory
│   │   ├── tasks.json      # Main task database
│   │   ├── task-1.md      # Individual task files
│   │   └── task-2.md
│   ├── docs/              # Documentation directory
│   │   ├── prd.txt        # Product requirements
│   ├── reports/           # Analysis reports directory
│   │   └── task-complexity-report.json
│   ├── templates/         # Template files
│   │   └── example_prd.txt  # Example PRD template
│   └── config.json        # AI models & settings
├── .claude/
│   ├── settings.json      # Claude Code configuration
│   └── commands/         # Custom slash commands
├── .env                  # API keys
├── .mcp.json            # MCP configuration
└── CLAUDE.md            # This file - auto-loaded by Claude Code
```

## MCP Integration

Task Master provides an MCP server that Claude Code can connect to. Configure in `.mcp.json`:

```json
{
  "mcpServers": {
    "task-master-ai": {
      "command": "npx",
      "args": ["-y", "--package=task-master-ai", "task-master-ai"],
      "env": {
        "ANTHROPIC_API_KEY": "your_key_here",
        "PERPLEXITY_API_KEY": "your_key_here",
        "OPENAI_API_KEY": "OPENAI_API_KEY_HERE",
        "GOOGLE_API_KEY": "GOOGLE_API_KEY_HERE",
        "XAI_API_KEY": "XAI_API_KEY_HERE",
        "OPENROUTER_API_KEY": "OPENROUTER_API_KEY_HERE",
        "MISTRAL_API_KEY": "MISTRAL_API_KEY_HERE",
        "AZURE_OPENAI_API_KEY": "AZURE_OPENAI_API_KEY_HERE",
        "OLLAMA_API_KEY": "OLLAMA_API_KEY_HERE"
      }
    }
  }
}
```

### Essential MCP Tools

```javascript
help; // = shows available taskmaster commands
// Project setup
initialize_project; // = task-master init
parse_prd; // = task-master parse-prd

// Daily workflow
get_tasks; // = task-master list
next_task; // = task-master next
get_task; // = task-master show <id>
set_task_status; // = task-master set-status

// Task management
add_task; // = task-master add-task
expand_task; // = task-master expand
update_task; // = task-master update-task
update_subtask; // = task-master update-subtask
update; // = task-master update

// Analysis
analyze_project_complexity; // = task-master analyze-complexity
complexity_report; // = task-master complexity-report
```

## Claude Code Workflow Integration

### Standard Development Workflow

#### 1. Project Initialization

```bash
# Initialize Task Master
task-master init

# Create or obtain PRD, then parse it
task-master parse-prd .taskmaster/docs/prd.txt

# Analyze complexity and expand tasks
task-master analyze-complexity --research
task-master expand --all --research
```

If tasks already exist, another PRD can be parsed (with new information only!) using parse-prd with --append flag. This will add the generated tasks to the existing list of tasks..

#### 2. Daily Development Loop

```bash
# Start each session
task-master next                           # Find next available task
task-master show <id>                     # Review task details

# During implementation, check in code context into the tasks and subtasks
task-master update-subtask --id=<id> --prompt="implementation notes..."

# Complete tasks
task-master set-status --id=<id> --status=done
```

#### 3. Multi-Claude Workflows

For complex projects, use multiple Claude Code sessions:

```bash
# Terminal 1: Main implementation
cd project && claude

# Terminal 2: Testing and validation
cd project-test-worktree && claude

# Terminal 3: Documentation updates
cd project-docs-worktree && claude
```

### Custom Slash Commands

Create `.claude/commands/taskmaster-next.md`:

```markdown
Find the next available Task Master task and show its details.

Steps:

1. Run `task-master next` to get the next task
2. If a task is available, run `task-master show <id>` for full details
3. Provide a summary of what needs to be implemented
4. Suggest the first implementation step
```

Create `.claude/commands/taskmaster-complete.md`:

```markdown
Complete a Task Master task: $ARGUMENTS

Steps:

1. Review the current task with `task-master show $ARGUMENTS`
2. Verify all implementation is complete
3. Run any tests related to this task
4. Mark as complete: `task-master set-status --id=$ARGUMENTS --status=done`
5. Show the next available task with `task-master next`
```

## Tool Allowlist Recommendations

Add to `.claude/settings.json`:

```json
{
  "allowedTools": [
    "Edit",
    "Bash(task-master *)",
    "Bash(git commit:*)",
    "Bash(git add:*)",
    "Bash(npm run *)",
    "mcp__task_master_ai__*"
  ]
}
```

## Configuration & Setup

### API Keys Required

At least **one** of these API keys must be configured:

- `ANTHROPIC_API_KEY` (Claude models) - **Recommended**
- `PERPLEXITY_API_KEY` (Research features) - **Highly recommended**
- `OPENAI_API_KEY` (GPT models)
- `GOOGLE_API_KEY` (Gemini models)
- `MISTRAL_API_KEY` (Mistral models)
- `OPENROUTER_API_KEY` (Multiple models)
- `XAI_API_KEY` (Grok models)

An API key is required for any provider used across any of the 3 roles defined in the `models` command.

### Model Configuration

```bash
# Interactive setup (recommended)
task-master models --setup

# Set specific models
task-master models --set-main claude-3-5-sonnet-20241022
task-master models --set-research perplexity-llama-3.1-sonar-large-128k-online
task-master models --set-fallback gpt-4o-mini
```

## Task Structure & IDs

### Task ID Format

- Main tasks: `1`, `2`, `3`, etc.
- Subtasks: `1.1`, `1.2`, `2.1`, etc.
- Sub-subtasks: `1.1.1`, `1.1.2`, etc.

### Task Status Values

- `pending` - Ready to work on
- `in-progress` - Currently being worked on
- `done` - Completed and verified
- `deferred` - Postponed
- `cancelled` - No longer needed
- `blocked` - Waiting on external factors

### Task Fields

```json
{
  "id": "1.2",
  "title": "Implement user authentication",
  "description": "Set up JWT-based auth system",
  "status": "pending",
  "priority": "high",
  "dependencies": ["1.1"],
  "details": "Use bcrypt for hashing, JWT for tokens...",
  "testStrategy": "Unit tests for auth functions, integration tests for login flow",
  "subtasks": []
}
```

## Claude Code Best Practices with Task Master

### Context Management

- Use `/clear` between different tasks to maintain focus
- This CLAUDE.md file is automatically loaded for context
- Use `task-master show <id>` to pull specific task context when needed

### Iterative Implementation

1. `task-master show <subtask-id>` - Understand requirements
2. Explore codebase and plan implementation
3. `task-master update-subtask --id=<id> --prompt="detailed plan"` - Log plan
4. `task-master set-status --id=<id> --status=in-progress` - Start work
5. Implement code following logged plan
6. `task-master update-subtask --id=<id> --prompt="what worked/didn't work"` - Log progress
7. `task-master set-status --id=<id> --status=done` - Complete task

### Complex Workflows with Checklists

For large migrations or multi-step processes:

1. Create a markdown PRD file describing the new changes: `touch task-migration-checklist.md` (prds can be .txt or .md)
2. Use Taskmaster to parse the new prd with `task-master parse-prd --append` (also available in MCP)
3. Use Taskmaster to expand the newly generated tasks into subtasks. Consdier using `analyze-complexity` with the correct --to and --from IDs (the new ids) to identify the ideal subtask amounts for each task. Then expand them.
4. Work through items systematically, checking them off as completed
5. Use `task-master update-subtask` to log progress on each task/subtask and/or updating/researching them before/during implementation if getting stuck

### Git Integration

Task Master works well with `gh` CLI:

```bash
# Create PR for completed task
gh pr create --title "Complete task 1.2: User authentication" --body "Implements JWT auth system as specified in task 1.2"

# Reference task in commits
git commit -m "feat: implement JWT auth (task 1.2)"
```

### Parallel Development with Git Worktrees

```bash
# Create worktrees for parallel task development
git worktree add ../project-auth feature/auth-system
git worktree add ../project-api feature/api-refactor

# Run Claude Code in each worktree
cd ../project-auth && claude    # Terminal 1: Auth work
cd ../project-api && claude     # Terminal 2: API work
```

## Troubleshooting

### AI Commands Failing

```bash
# Check API keys are configured
cat .env                           # For CLI usage

# Verify model configuration
task-master models

# Test with different model
task-master models --set-fallback gpt-4o-mini
```

### MCP Connection Issues

- Check `.mcp.json` configuration
- Verify Node.js installation
- Use `--mcp-debug` flag when starting Claude Code
- Use CLI as fallback if MCP unavailable

### Task File Sync Issues

```bash
# Regenerate task files from tasks.json
task-master generate

# Fix dependency issues
task-master fix-dependencies
```

DO NOT RE-INITIALIZE. That will not do anything beyond re-adding the same Taskmaster core files.

## Important Notes

### AI-Powered Operations

These commands make AI calls and may take up to a minute:

- `parse_prd` / `task-master parse-prd`
- `analyze_project_complexity` / `task-master analyze-complexity`
- `expand_task` / `task-master expand`
- `expand_all` / `task-master expand --all`
- `add_task` / `task-master add-task`
- `update` / `task-master update`
- `update_task` / `task-master update-task`
- `update_subtask` / `task-master update-subtask`

### File Management

- Never manually edit `tasks.json` - use commands instead
- Never manually edit `.taskmaster/config.json` - use `task-master models`
- Task markdown files in `tasks/` are auto-generated
- Run `task-master generate` after manual changes to tasks.json

### Claude Code Session Management

- Use `/clear` frequently to maintain focused context
- Create custom slash commands for repeated Task Master workflows
- Configure tool allowlist to streamline permissions
- Use headless mode for automation: `claude -p "task-master next"`

### Multi-Task Updates

- Use `update --from=<id>` to update multiple future tasks
- Use `update-task --id=<id>` for single task updates
- Use `update-subtask --id=<id>` for implementation logging

### Research Mode

- Add `--research` flag for research-based AI enhancement
- Requires a research model API key like Perplexity (`PERPLEXITY_API_KEY`) in environment
- Provides more informed task creation and updates
- Recommended for complex technical tasks

---

_This guide ensures Claude Code has immediate access to Task Master's essential functionality for agentic development workflows._

---

## Upstream Gemini CLI Sync (2025-08)
Latest highlights from https://github.com/google-gemini/gemini-cli to supplement this project integration guide.

### Installation Options
```bash
# Run instantly (ephemeral)
npx https://github.com/google-gemini/gemini-cli

# Global npm install
npm install -g @google/gemini-cli

# Homebrew (macOS / Linux)
brew install gemini-cli
```
Requirements: Node.js >= 20

### Authentication Modes
1. OAuth (Google Account) – default interactive login; free tier ~60 req/min & 1,000 req/day.
2. API Key – export GEMINI_API_KEY (or GOOGLE_API_KEY); more granular model choice.
3. Vertex AI – enterprise usage; set GOOGLE_API_KEY or service auth plus export GOOGLE_GENAI_USE_VERTEXAI=true and optionally GOOGLE_CLOUD_PROJECT.

Environment examples:
```bash
# OAuth: just run `gemini` and follow browser flow
export GOOGLE_CLOUD_PROJECT="your-project"   # (optional for enterprise / paid)

# API key
export GEMINI_API_KEY="YOUR_API_KEY"         # aistudio.google.com/apikey

# Vertex AI
export GOOGLE_API_KEY="YOUR_API_KEY"
export GOOGLE_GENAI_USE_VERTEXAI=true
export GOOGLE_CLOUD_PROJECT="your-project"
```

### Core CLI Usage
```bash
gemini                                   # Start interactive in current dir
gemini --include-directories ../lib,../docs
gemini -m gemini-2.5-flash               # Select model
gemini -p "Explain the architecture"      # One-shot non-interactive
```
Add relevant files/context with a project GEMINI.md (this file) and other sources. Supports very large (1M token) contexts with 2.5 Pro.

### Key Built‑in Capabilities
- Google Search grounding for real-time info
- Conversation checkpointing (save / resume long sessions)
- Memory via GEMINI.md + token caching
- Multimodal input (code, text, images, PDFs) → code generation / scaffolding
- Tooling: file ops, shell commands, web fetch, multi-file edits
- MCP integration for external tools (e.g., media generation, custom servers)
- GitHub Action for PR review & issue triage (`@gemini-cli` mentions)

### Productivity Features
- Slash commands (/help, /chat, /mcp, /bug, etc.) – see upstream commands reference
- Keyboard shortcuts & theming (docs/keyboard-shortcuts.md, docs/cli/themes.md)
- Checkpointing & memory management to navigate large conversations without losing context

### When to Prefer Gemini CLI vs Task Master
| Use Gemini CLI for | Use Task Master for |
| ------------------ | ------------------- |
| Exploration, quick Q&A, refactors | Structured backlog & dependency mgmt |
| Multimodal code understanding | Persistent task logging & progress notes |
| Real-time grounded research | Complexity analysis & subtask expansion |
| PR review automation | Cross-tag initiative planning |

### Combined Workflow Pattern
1. Start Gemini CLI interactive session in repo root: `gemini`
2. Ask architectural / refactor questions; capture decisions.
3. Log decisions into tasks/subtasks with `task-master update-subtask`.
4. Implement code; iterate using Gemini CLI for localized reasoning.
5. Finalize: mark tasks done; checkpoint conversation if useful.

### Security & Telemetry Notes
- Review telemetry docs if sensitive code: ability to opt-out or adjust.
- Sandbox & file write operations are constrained; confirm before executing dangerous commands.

### Quick Troubleshooting
| Symptom | Action |
| ------- | ------ |
| OAuth loop / browser not opening | Use API key mode (export GEMINI_API_KEY). |
| Large context truncated | Split prompt, use checkpointing, trim files. |
| Rate limit | Switch auth mode (Vertex / paid) or batch requests. |
| Missing tools | Verify `.gemini/settings.json` and restart session. |

### Reference Links
- Quickstart: docs/cli/index.md
- Auth: docs/cli/authentication.md
- Configuration & Environment: docs/cli/configuration.md
- Commands: docs/cli/commands.md
- MCP Server Integration: docs/tools/mcp-server.md
- Checkpointing: docs/checkpointing.md
- Token Caching: docs/cli/token-caching.md
- Troubleshooting: docs/troubleshooting.md
- Roadmap: ROADMAP.md

(Sections condensed; consult upstream README for full details.)

---

## Gemini CLI Command Usage Cheat Sheet (요약 사용법)
Quick, condensed reference of practical commands from the upstream docs. (Summaries, not verbatim.)

### 1. Slash Commands 핵심 (/)
| Command | Purpose (EN) | 용도 (KR) |
|---------|--------------|-----------|
| /help | List available commands & tools | 명령/도구 목록 표시 |
| /bug <title> | Open issue (headline = title) | 버그 이슈 생성 |
| /chat save <tag> | Save convo checkpoint | 대화 체크포인트 저장 |
| /chat resume <tag> | Resume saved checkpoint | 저장된 상태 복원 |
| /chat list | List checkpoints | 체크포인트 목록 |
| /chat delete <tag> | Delete checkpoint | 체크포인트 삭제 |
| /clear | Clear screen | 화면 지우기 |
| /compress | Summarize & replace full context | 전체 문맥 요약/압축 |
| /copy | Copy last model output | 마지막 출력 복사 |
| /directory add <paths> | Add dirs to context | 디렉터리 컨텍스트 추가 |
| /directory show | Show included dirs | 포함된 디렉터리 표시 |
| /mcp | List MCP servers + tools | MCP 서버/도구 목록 |
| /mcp desc | Show tool descriptions | 도구 설명 표시 |
| /mcp nodesc | Hide descriptions | 설명 숨기기 |
| /mcp schema | Show tool JSON schema | 도구 스키마 표시 |
| /memory add <text> | Append persistent memory | 메모리 추가 |
| /memory show | Show loaded memory text | 메모리 내용 표시 |
| /memory refresh | Reload GEMINI.md hierarchy | GEMINI.md 재로딩 |
| /restore [id] | Restore pre-tool file snapshot | 도구 실행 전 상태 복구 |
| /stats | Session stats & token usage | 토큰/세션 통계 |
| /theme | Theme selection dialog | 테마 변경 |
| /auth | Switch auth method | 인증 방식 변경 |
| /about | Version/build info | 버전 정보 |
| /tools | List available runtime tools | 사용 가능 도구 목록 |
| /tools desc | Show tool descriptions | 도구 설명 표시 |
| /tools nodesc | Hide tool descriptions | 설명 숨기기 |
| /privacy | Privacy notice & consent | 프라이버시 설정 |
| /quit (/exit) | Exit CLI | 종료 |
| /vim | Toggle Vim key mode | Vim 모드 전환 |
| /init | Auto-generate GEMINI.md template | GEMINI.md 자동 생성 |

### 2. @ Commands (파일/디렉터리 컨텍스트)
| Pattern | Description |
|---------|-------------|
| @path/to/file | Inject file content into current prompt |
| @path/to/dir | Recursively inject (git-ignored filtered) text files |
| mixed prompt @README.md question | File appended even mid-sentence |
| @ (alone) | Literal '@' (no injection) |

Notes:
- Respects gitignore for noise reduction.
- Large/binary may be truncated or skipped.
- Escape spaces: @My\ Documents/file.txt

### 3. ! Shell Commands (! 명령)
| Form | Behavior |
|------|----------|
| !ls -la | Execute one-off command then return to chat |
| !git status | Same; environment has GEMINI_CLI=1 |
| ! (alone) | Toggle persistent shell mode (subsequent lines run as shell until exiting) |

Safety: Treat as normal shell — destructive commands still destructive.

### 4. Custom Commands (사용자 정의 TOML)
Location precedence:
1. Global: ~/.gemini/commands/
2. Project: <repo>/.gemini/commands/ (overrides global on name collision)

Naming:
- File path segments -> namespace with ':'
  - .gemini/commands/git/commit.toml => /git:commit

TOML Required Field:
- prompt = "..." (multi-line allowed)
Optional:
- description = "..."

Argument Handling:
1. {{args}} placeholder → replaced verbatim with user arguments.
2. No {{args}} → CLI appends the full invoked command after two newlines.

Dynamic Context Injection:
- Use !{command here} inside prompt to run shell & inline output (will prompt for confirmation; can allow once or session).

Example Skeleton:
```toml
# ~/.gemini/commands/refactor/pure.toml
description = "Refactor code into a pure function"
prompt = """
Please refactor the provided code into a pure function.
Return:
1. Refactored code block
2. Bullet list of key purity changes
"""
```

### 5. Workflow Pairing with Task Master
| Need | Gemini CLI | Task Master |
|------|------------|-------------|
| Quick exploration | /chat + @files | (N/A) |
| Persist plan | /compress or memory add | update-subtask append |
| Multi-task refactor | custom /refactor:* commands | expand / dependencies |
| Roll back file edits | /restore | (N/A) |
| Track decision rationale | /memory add | update-subtask notes |

### 6. Security & Governance Tips
- Review /privacy before adding customer data to prompts.
- Gate any custom commands executing !{git diff} or secrets with human review.
- Prefer /compress to reduce token cost on long sessions.
- Keep GEMINI.md small & layered (root + feature subdirs) for faster reloads.

### 7. Quick Korean One-liner Prompts
| Goal | Prompt (KR) |
|------|-------------|
| Summarize module | "@src/module/ 이 디렉터리 구조와 책임을 요약해줘" |
| Plan refactor | "@src/service/UserService.java 이 클래스 리팩터링 단계별 계획 생성" |
| Generate tests | "@src/service/UserService.java 주요 public 메소드 단위 테스트 코드 작성" |
| Explain diff | "!git diff --staged 최근 변경 요약 및 리스크 설명" |
| Optimize query | "@src/repository/ContentRepository.java JPA/QueryDSL 성능 개선 아이디어" |

---
Additions above are distilled; see upstream docs for exhaustive detail.

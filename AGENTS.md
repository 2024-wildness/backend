git commit -m "feat: implement JWT auth (task 1.2)"
git worktree add ../project-auth feature/auth-system
git worktree add ../project-api feature/api-refactor
task-master models
task-master models --set-fallback gpt-4o-mini
task-master generate
task-master fix-dependencies
## Canonical Backlog by Domain (CTO Directive)

This file is the single source of truth for work planning. We organize by domain (package-by-feature) and cut vertical slices that deliver end-to-end value. The Taskmaster master tag currently holds foundational tasks; cross-links are included below.

### Conventions
- Status: Proposed | Ready | In Progress | Blocked | Done
- Priority: P0 (must) | P1 (should) | P2 (could)
- Links: Taskmaster #[ID] (master tag unless stated), PRs, ADRs
- DoR (Definition of Ready): user story, AC, dependencies, estimate present
- DoD (Definition of Done): code merged, tests passing, observability added, docs/ADR updated

### Item Template
- Title: [Domain] Short feature/bug title
- User story: As a <role>, I want <capability> so that <outcome>.
- Acceptance criteria:
  1) ...
  2) ...
  3) ...
- Non-functional: perf/security/observability as applicable
- Dependencies: Taskmaster #[ID], services, schemas
- Estimate: S/M/L
- Priority: P0/P1/P2
- Status: Proposed
- Links: related PRs/ADRs/Tasks

---

## Auth
1) [Auth] Session token issuance & validation (vertical)
- Story: As a user, I can sign in and receive a JWT to access protected APIs.
- AC: 1) Valid creds -> 200 with signed JWT; 2) Bearer requests authorized; 3) Expired/invalid -> 401; 4) Audit log on sign-in.
- Non-functional: 60m TTL, ±2m clock skew tolerance, key rotation ready.
- Dependencies: Taskmaster #2 (User Authentication), #1.4 (config), #1.6 (health baseline).
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #2, #1.6

2) [Auth] Password reset flow (vertical)
- Story: As a user, I can self-serve password reset via emailed one-time link.
- AC: 1) POST request always 200; 2) One-time token TTL ≥15m; 3) Confirm updates password and invalidates sessions; 4) Rate-limit 5/hr/IP.
- Non-functional: Tokens stored hashed; SES or LocalStack in dev.
- Dependencies: Taskmaster #3 (Password Reset)
- Estimate: M
- Priority: P1
- Status: Proposed
- Links: Taskmaster #3

## Category
1) [Category] CRUD categories with ordering
- AC: create/list/update/delete; unique per user; reorder via position; optimistic concurrency (ETag).
- Dependencies: Taskmaster #4 (Category CRUD)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #4

2) [Category] Bulk assign items to category
- AC: batch up to 100 ids; partial failures reported; 200 with per-item results.
- Dependencies: Content domain basic CRUD
- Estimate: S
- Priority: P1
- Status: Proposed

## Content
1) [Content] Create and edit rich text items
- AC: bold/italic/links persisted; autosave drafts; max 50KB; server validation.
- Dependencies: Taskmaster #5 (Content CRUD)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #5

2) [Content] Version history and restore
- AC: version on update; list versions; restore previous creates new version; audit on restore.
- Dependencies: #5
- Estimate: M
- Priority: P1
- Status: Proposed

## Share
1) [Share] Public share link with scopes
- AC: create token with expiry; GET /share/{token} serves resource or 410 on invalid/expired; logs access.
- Dependencies: Taskmaster #6 (Shareable Links)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #6

2) [Share] Invite collaborator by email (RW)
- AC: invite -> email; accept grants RW; owner can revoke; activity logged.
- Dependencies: Auth, Email
- Estimate: M
- Priority: P2
- Status: Proposed

## Reminder
1) [Reminder] Schedule time-based reminders
- AC: create with future KST time; scheduler dispatches; idempotent send (sent_at set).
- Dependencies: Taskmaster #8 (Reminders & Emails)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #8

2) [Reminder] Snooze and dismiss actions
- AC: snooze updates schedule; dismiss cancels; list reflects state within 2s.
- Estimate: S
- Priority: P1
- Status: Proposed

## Search
1) [Search] Full-text search over content
- AC: index on create/update/delete; GET /api/search returns ranked results; p95 <300ms for 10k docs.
- Dependencies: Taskmaster #7 (Meili + Outbox)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #7

2) [Search] Faceted filter by category and date
- AC: filters on userId+category+date; pagination; empty filters return all.
- Estimate: S
- Priority: P1
- Status: Proposed

## Stats
1) [Stats] Daily aggregation and API
- AC: nightly KST job persists per-user daily rows; GET /api/stats/daily returns last 30 days.
- Dependencies: Taskmaster #9 (Statistics Aggregation)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #9

2) [Stats] Weekly/monthly rollups
- AC: aggregated endpoints; input validation; performance within p95 300ms.
- Dependencies: #9 daily foundation
- Estimate: S
- Priority: P1
- Status: Proposed

## AI
1) [AI] Summarize content item
- AC: POST /api/content/{id}/ai-analyze; max input 10k chars; result stored; retries on 429.
- Dependencies: Taskmaster #10 (AI Summarization & Auto-Tagging)
- Estimate: M
- Priority: P0
- Status: Proposed
- Links: Taskmaster #10

2) [AI] Category suggestion model
- AC: suggest top 3 categories w/ confidence; feedback loop stored.
- Estimate: S
- Priority: P1
- Status: Proposed

---

Notes
- Trunk-Based Development: small batches; merge to main daily.
- Quality Gates (CI): Spotless + Checkstyle + Tests; mutation where feasible.
- Architecture: Spring Modulith for domain boundaries; enforce with module tests.

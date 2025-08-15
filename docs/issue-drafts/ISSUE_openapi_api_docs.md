# Issue: Add OpenAPI (springdoc) Documentation & Swagger UI

## Summary
Introduce springdoc-openapi to expose interactive API documentation (Swagger UI) and ship an initial consolidated `API_SPECIFICATION.md` synced with current controllers.

## Context
Implemented in branch: `feature/domain-auditing-refactor`
Commit introducing change: 48b8962 (feat(api-docs): introduce springdoc-openapi and swagger ui)
Default base branch: `dev`

## Changes Implemented
- Added dependency: `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0`
- Added `OpenApiConfig` with JWT Bearer security scheme (`bearer-jwt`)
- Updated `SecurityConfig` to permit `/v3/api-docs/**`, `/swagger-ui.html`, `/swagger-ui/**`
- Added initial `API_SPECIFICATION.md` (manually curated) for frontend onboarding

## Rationale / Value
- Enables frontend & external integrators to self-discover endpoints
- Reduces drift vs. code by centralizing an editable spec file
- Simplifies QA & Postman collection generation

## Screens / URLs
- Swagger UI: `/swagger-ui/index.html`
- Raw JSON: `/v3/api-docs`
- Raw YAML: `/v3/api-docs.yaml`

## Follow-up Tasks (Proposed)
| ID | Task | Priority | Notes |
|----|------|----------|-------|
| 1 | Add profile-based conditional exposure (disable in prod by default) | Medium | Use `springdoc.api-docs.enabled=false` in prod | 
| 2 | Add global error response schema & examples | Medium | Standardize error envelope | 
| 3 | Tag grouping per domain (Auth, Category, Content, Photo) | Low | Use `@Tag` annotations | 
| 4 | Add explicit schema annotations where Lombok/records may hide nullability | Low | Clarify required fields | 
| 5 | Automate spec export to CI artifact | Low | For downstream clients | 
| 6 | Sync REST + GraphQL docs (explore federation doc approach) | Low | Future enhancement | 

## Acceptance Criteria
- [x] Swagger UI loads without authentication for public access
- [x] JWT Authorize button present; authenticated endpoints callable with token
- [x] Basic API info (title/version) visible
- [x] API_SPECIFICATION.md committed with current endpoints

## Risks / Mitigations
| Risk | Impact | Mitigation |
|------|--------|------------|
| Accidental exposure in prod | Medium | Add prod profile guard (follow-up task) |
| Spec drift (manual vs generated) | Low | Periodic review + future code-first annotations |

## Requested Action
Create PR from `feature/domain-auditing-refactor` into `dev` and merge after review. This issue documents the change set.

---
Prepared automatically.

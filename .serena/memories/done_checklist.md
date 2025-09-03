Before Marking a Task Done

- Code formatted and linted: `./gradlew spotlessApply check`
- Tests updated/added and passing: `./gradlew test`
- DB changes have Flyway migration scripts in `src/main/resources/db/migration`
- Local run sanity: start with `make dev` and verify /actuator/health
- API changes documented: verify springdoc renders correctly in dev
- Commit/PR: Conventional Commit message; PR includes description, linked issue, notes about migrations/ops; attach logs/screenshots for behavior changes
- Optional: build container (`make build` or `./gradlew jibDockerBuild`) and run `make smoke`/`make health`
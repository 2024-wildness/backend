# Repository Guidelines

## Project Structure & Module Organization

- `src/main/java`: Spring Boot app (packages under `com.madiest.moapin.*`).
- `src/test/java`: Unit/integration tests (JUnit 5).
- `src/main/resources`: Spring config and assets (`application.yml`, profile overrides, `db/migration` for Flyway).
- `config/checkstyle`: Checkstyle rules; enforced in CI/`gradle check`.
- `docs/`: Working notes and drafts.
- Build tooling: `build.gradle`, `gradlew`, `Makefile`, `Dockerfile`, `compose.yaml`.

## Build, Test, and Development Commands

- `./gradlew bootRun` or `make dev`: Run app locally with `dev` profile.
- `./gradlew test` or `make test`: Execute tests (JUnit 5).
- `./gradlew clean bootJar -x test` or `make jar`: Build runnable JAR.
- `make build`: Build Docker image from Dockerfile.
- `./gradlew jibDockerBuild` or `make jib`: Build Docker image via Jib (no daemon required for tar/push tasks).
- `docker compose up -d` or `make up`: Start app + infra (Postgres, Redis, RabbitMQ).
- `make infra-up`: Start only infra services.

## Coding Style & Naming Conventions

- Formatting: Spotless with `googleJavaFormat` (run via `./gradlew spotlessApply`).
- Linting: Checkstyle (`config/checkstyle/checkstyle.xml`) runs in `gradle check`.
- Naming: Classes `PascalCase`, methods/fields `camelCase`, constants `UPPER_SNAKE_CASE`.
- Packages: Use `com.madiest.moapin.<feature>` (e.g., `content`, `auth`, `search`).

## Testing Guidelines

- Framework: JUnit 5; Spring test utilities available.
- Conventions: Unit tests end with `*Test`; integration tests `*IntegrationTest`.
- Run: `./gradlew test` (profiles auto-configured with embedded H2 where applicable).

## Commit & Pull Request Guidelines

- Commit style: Conventional Commits (e.g., `feat:`, `fix:`, `chore:`). Example:
    - `feat(domain): introduce Flyway baseline & unify auditing`
- PRs: Provide clear description, link related issues, include screenshots/logs for behavior changes, and note any
  migration impacts (Flyway).
- Keep PRs focused; include test updates and migration scripts under `src/main/resources/db/migration` when schema
  changes.

## Security & Configuration Tips

- Profiles: `dev`, `test`, `prod` via `SPRING_PROFILES_ACTIVE` (see `application-*.yml`).
- Secrets: Prefer environment variables or compose overrides; do not commit secrets.
- Health: Check `GET /actuator/health` (see `make health`).

## Architecture Overview (Brief)

- Spring Boot REST API with modules for `auth`, `content`, `category`, `search`, `reminder`, and `share`.
- Persistence: Spring Data JPA + Flyway migrations; outbox events for search indexing.

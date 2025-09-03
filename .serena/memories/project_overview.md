Project: Moapin Backend (Java/Spring Boot)

Purpose

- Provide REST APIs for authentication, content (notes/links/photos), categories, reminders, search indexing, and share
  links.

Tech Stack

- Language: Java 21
- Frameworks: Spring Boot 3.5 (Web, Security, Data JPA, Actuator, Validation)
- Persistence: PostgreSQL (prod), H2 (tests), Flyway for DB migrations
- Search/Infra: Meilisearch client, Redis, RabbitMQ (outbox/events)
- Auth/Security: JWT (jjwt), Spring Security
- Build: Gradle, Jib or Dockerfile for container images
- Tooling: Spotless (googleJavaFormat), Checkstyle, Springdoc OpenAPI

Structure

- src/main/java: app code under packages `com.madiest.moapin.*` (auth, content, category, search, reminder, share,
  common/config)
- src/test/java: unit/integration tests (JUnit 5)
- src/main/resources: application.yml (+ profiles), Flyway migrations in db/migration
- config/checkstyle: checkstyle rules
- Build/ops: build.gradle, gradlew, Makefile, Dockerfile, compose.yaml

Entrypoints

- Main class: com.madiest.moapin.MoapinApplication
- Run local: `SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun` or `make dev`
- Health: GET /actuator/health
- OpenAPI UI: via springdoc at /swagger-ui/index.html (dev)

Code Style & Conventions

- Formatting: Spotless with googleJavaFormat. Apply with `./gradlew spotlessApply`.
- Linting: Checkstyle (config/checkstyle/checkstyle.xml). Runs in `./gradlew check`.
- Java version: 21 (toolchain configured in build.gradle).
- Naming: Classes PascalCase; methods/fields camelCase; constants UPPER_SNAKE_CASE; packages
  `com.madiest.moapin.<feature>`.
- Tests: JUnit 5. Unit tests end with *Test; integration tests end with *IntegrationTest.
- Migrations: Use Flyway. Place SQL in src/main/resources/db/migration using `V{version}__description.sql`.
- REST: Spring MVC controllers under feature packages; validate requests with javax/jakarta validation; document via
  springdoc-openapi.
- Profiles: dev/test/prod via `SPRING_PROFILES_ACTIVE` and `application-*.yml`.

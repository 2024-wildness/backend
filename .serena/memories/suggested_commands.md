Common Commands

Development

- Run (dev profile): `SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun` or `make dev`
- Tests: `./gradlew test` or `make test`
- Build JAR (skip tests): `./gradlew clean bootJar -x test` or `make jar`
- Format: `./gradlew spotlessApply`; Lint: `./gradlew check`

Containers & Infra

- Build image (Dockerfile): `make build`
- Build image (Jib): `./gradlew jibDockerBuild` or `make jib`
- Start full stack: `docker compose up -d` or `make up`
- Start only infra: `make infra-up`; stop: `make infra-down`
- Health check: `make health` (GET /actuator/health)

Release/Registry (optional)

- Build + push with git SHA tag: `make registry-all` (requires docker login and REGISTRY configured)

[English](README.md) | [한국어](README.ko.md)

# Moapin Backend

Spring Boot 3.5.x (Java 21 toolchain) service.

## 1. Quick Start
```bash
# (Dev profile, in-memory H2, Swagger enabled)
./gradlew bootRun --args='--spring.profiles.active=dev'
```
Then open:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 2. Run Modes
| Mode | When to Use | Command | Notes |
|------|-------------|---------|-------|
| Gradle Dev | Fast iteration | `./gradlew bootRun --args='--spring.profiles.active=dev'` | Auto restart if DevTools present |
| Executable JAR | Local prod-ish | `./gradlew clean bootJar` then `java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev` | Add `--spring.docker.compose.enabled=true` to auto-start compose services |
| Docker Compose | Full stack (Postgres/Redis/RabbitMQ) | `docker compose up -d` | App service uses `SPRING_PROFILES_ACTIVE=prod` by default |
| Buildpacks | OCI image w/out Dockerfile | `./gradlew bootBuildImage --imageName=moapin-app:pack` | Uses Paketo buildpacks |
| Jib | Fast image build | `./gradlew jibDockerBuild -x test` | Produces `moapin-app:jib` locally |
| Native (Optional) | Startup / memory optimization | `./gradlew nativeCompile` | Produces binary in `build/native/nativeCompile/` |

### 2.1 Enabling Docker Compose from JAR
```bash
java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar \
	--spring.profiles.active=dev \
	--spring.docker.compose.enabled=true
```
If `compose.yaml` defines databases/brokers they will start; otherwise app falls back to H2.

## 3. Profiles
| Profile | Purpose | DB | Swagger | Notes |
|---------|---------|----|---------|-------|
| dev | Local development | H2 (in‑memory) | Enabled | Fast boot, relaxed settings |
| prod | Production / Compose | Postgres | Disabled (planned) | External services (Redis/RabbitMQ) |

## 4. Tests
```bash
./gradlew test
```
REST Docs / snippets (if enabled) go to `build/generated-snippets`.

## 5. Database & Migrations
- Flyway auto-applies SQL in `src/main/resources/db/migration` (e.g. V1__*.sql)
- Dev uses H2: `jdbc:h2:mem:moapin` (no persistence between runs)
- Prod/Compose uses Postgres (see compose env vars)
- To repair/migrate issues: `./gradlew bootRun --args='--spring.flyway.repair=true'`

## 6. Swagger / OpenAPI
- Library: `springdoc-openapi-starter-webmvc-ui`
- Dev URL: `/swagger-ui/index.html`
- Add `Authorization: Bearer <token>` in Swagger Authorize dialog (Scheme: bearer-jwt)
- For production disable UI via profile override (example in `application-prod.yml` if added later).

## 7. Security & JWT
- JWT secret accepts RAW or Base64; property path: `app.security.jwt.secret` (check your config class if different)
- Generated Spring Boot fallback password appears if custom auth not fully wired (seen once at cold start); ensure real users for prod.
- Actuator endpoints are secured except those explicitly permitted.

## 8. Building Artifacts
```bash
# JAR
./gradlew clean bootJar

# OCI Image (Buildpacks)
./gradlew bootBuildImage --imageName=moapin-app:pack

# OCI Image (Jib, no Docker daemon needed for tar)
./gradlew jibDockerBuild -x test

# Native binary
./gradlew nativeCompile
```

## 9. Makefile Shortcuts
```bash
make jar          # Build bootJar (skip tests)
make build        # jar + docker build (app image)
make jib          # Build image via Jib
make dev          # Run dev profile via Gradle (bootRun)
make run          # Run built jar (auto-build if missing) with dev profile
make smoke        # One‑shot: build jar -> start -> health check -> shutdown
make infra-up     # Start postgres + redis + rabbitmq only
make infra-down   # Stop infra services
make compose-only # Build image then start only app container (reuse running infra)
make up           # docker compose up -d (full stack as defined)
make logs         # follow app logs (compose service)
make down         # docker compose down -v
make health       # curl actuator health (localhost:8080)
```
Health check target fails with non‑zero exit if service unhealthy.

## 10. Key Environment Variables (Compose)
| Variable | Default (compose) | Purpose |
|----------|-------------------|---------|
| SPRING_PROFILES_ACTIVE | prod | Activate prod profile |
| SPRING_DATASOURCE_URL | jdbc:postgresql://postgres:5432/moapin | DB URL |
| SPRING_DATASOURCE_USERNAME | moapin | DB user |
| SPRING_DATASOURCE_PASSWORD | moapin | DB password |
| SPRING_REDIS_HOST | redis | Redis host |
| SPRING_RABBITMQ_HOST | rabbitmq | Broker host |

Additional (set as needed):
| Purpose | Property |
|---------|----------|
| JWT Secret | `APP_SECURITY_JWT_SECRET` (or matching env binding) |
| AWS S3 Region | `AWS_REGION` |
| Meilisearch Host | `MEILISEARCH_HOST` |

## 11. Logging & Tracing
- MDC keys: `requestId`, `userId`
- Incoming `X-Request-Id` reused or generated
- Consider enabling JSON layout for centralized logging (future enhancement)

## 12. Troubleshooting
| Symptom | Likely Cause | Fix |
|---------|--------------|-----|
| Flyway validation error | Changed past migration | Create new migration instead of editing old; use `repair` only if checksum drift intentional |
| H2 vs Postgres mismatch | Wrong profile | Pass `--spring.profiles.active=prod` or adjust compose env |
| Swagger 404 | UI disabled in profile | Ensure dev profile or enable property `springdoc.api-docs.enabled=true` |
| 401 on `/actuator/health` | Secured by Spring Security | Permit in `SecurityFilterChain` or use auth header |
| Generated password log line | Default user created | Provide real security config / ensure custom auth beans load |
| Port 8080 in use | Another process running | `lsof -i :8080` then kill PID / change `server.port` |

## 13. Command Cheat Sheet
```bash
# Dev run
./gradlew bootRun --args='--spring.profiles.active=dev'

# Jar run (dev)
java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Jar run w/ compose
java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --spring.docker.compose.enabled=true

# Full stack
docker compose up -d

# Stop stack
docker compose down -v

# Makefile quick flows
make smoke        # Fast local smoke validation
make infra-up && make run  # Bring infra then run app directly
make infra-down   # Tear down infra only
make build && docker run --rm -p 8080:8080 moapin-app:dev
```

## 15. CI / CD Workflows
| Workflow | Path | Trigger | Purpose | Key Steps |
|----------|------|---------|---------|-----------|
| Gradle Validate | `.github/workflows/ci-gradle.yml` | PR(dev), push(feature/**), manual | Quality gate (compile, static checks, tests) | Checkstyle, tests, bootJar, Trivy (fs) |
| Internal Fast Smoke | `.github/workflows/internal-smoke.yml` | PR(dev/main), push(feature/**) | Ultra-fast executable health smoke | compile → bootJar → run (dev) → /actuator/health |
| Docker Build & Push | `.github/workflows/docker-build-push.yml` | push(main/dev), manual | Build & (optional) push runtime image + security artifacts | bootJar, buildx, tags, Trivy image scan, SBOM |
| Release Tag Helper | `.github/workflows/release-tag.yml` | manual (dispatch) | Create annotated tag & optionally trigger image build | git tag, optional dispatch |

### 15.1 Image Tagging Strategy
- `latest` → main branch
- `dev` → dev branch
- `<12char sha>` default raw tag per commit
- Custom tag via manual workflow `imageTag` input or Release Tag Helper (`vX.Y.Z`)

### 15.2 SBOM & Vulnerabilities
- SBOM (SPDX JSON) uploaded as artifact `sbom`
- Trivy scans (filesystem + image) are non-blocking now. To enforce failing on HIGH/CRITICAL later, set `exit-code: 1` and add `severity: HIGH,CRITICAL`.

### 15.3 Fast vs Full Validation
| Layer | Target | Includes Tests | Security Scan | Image Build |
|-------|--------|----------------|---------------|-------------|
| Smoke | internal-smoke | No | No | No |
| Validate | ci-gradle | Yes | FS (Trivy) | Jar only |
| Image | docker-build-push | (skips tests) | Image (Trivy) + SBOM | Yes |

### 15.4 Local Reproduction
```bash
# Match smoke workflow locally
./gradlew compileJava -x checkstyleMain -x checkstyleTest
./gradlew bootJar -x test
SPRING_PROFILES_ACTIVE=dev java -jar build/libs/*SNAPSHOT.jar &
curl -fsS --retry 20 --retry-delay 2 http://localhost:8080/actuator/health
kill %1
```

## 16. Roadmap / Future Enhancements
- JSON logging appender option
- Harden security & secret management (Vault / AWS Secrets Manager)
- Observability: metrics dashboards, tracing exporter
- Production Swagger gating or removal
- Enforce Trivy severity gate (HIGH/CRITICAL)
- Add integrationTest task & promote into CI optional path
- Distroless / multi-arch image build (buildx)

---
Maintained using internal Gemini CLI workflow guidelines (see `.github/instructions/gemini_cli.instructions.md`).


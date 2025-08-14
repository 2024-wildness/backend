# Moapin Backend

Spring Boot 3 (Java 21) service.

## Build & Run (Local JVM)
```
./gradlew clean bootRun
```

## Tests
```
./gradlew test
```

## Containerization Options

### 1. Dockerfile (multi-stage)
Build JAR then image:
```
./gradlew clean bootJar -x test
docker build -t moapin-app:dev .
```
Run stack (app + postgres + redis + rabbitmq):
```
docker compose up -d
```
Check logs:
```
docker compose logs -f app
```
Shutdown:
```
docker compose down -v
```

### 2. Spring Boot Buildpacks
```
./gradlew bootBuildImage --imageName=moapin-app:pack
```

### 3. Jib (no Dockerfile needed)
```
./gradlew jibDockerBuild -x test
```
Produces local image `moapin-app:jib`.

## Makefile Shortcuts
```
make build        # jar + docker build
make up           # docker compose up -d
make logs         # follow app logs
make down         # tear down
make jib          # build image via Jib
make dev          # run with dev profile
```

## Environment Variables (compose app service)
- SPRING_PROFILES_ACTIVE=prod
- SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/moapin
- SPRING_DATASOURCE_USERNAME=moapin
- SPRING_DATASOURCE_PASSWORD=moapin
- SPRING_REDIS_HOST=redis
- SPRING_RABBITMQ_HOST=rabbitmq

## Logging & Tracing
MDC keys: `requestId`, `userId`. Incoming `X-Request-Id` is echoed back or generated.

## Future Enhancements
- Optional JSON logging appender
- Healthcheck endpoint exposure /actuator/health (ensure actuator enabled)
- Security hardening & secrets management

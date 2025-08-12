## Multi-stage build for Spring Boot application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Leverage layer caching: copy build scripts first
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
COPY gradlew.bat ./
RUN ./gradlew --version || true

# Copy source
COPY src ./src

# Build jar (skip tests for image creation speed)
RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre AS runtime
LABEL org.opencontainers.image.title="moapin" \
      org.opencontainers.image.description="Moapin Spring Boot service" \
      org.opencontainers.image.licenses="Apache-2.0"

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport" \
    SPRING_PROFILES_ACTIVE=prod

WORKDIR /app
RUN useradd --system --create-home --uid 1001 appuser
COPY --from=builder /workspace/build/libs/*SNAPSHOT.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

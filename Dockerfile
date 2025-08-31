## syntax=docker/dockerfile:1.7-labs
# ---------------------------------------------------------------------------
#  Moapin – External Build Pattern Dockerfile
#  Expectation: JAR is already built by CI/CD (./gradlew clean bootJar) and present at build/libs/
#  Rationale: Faster image builds, clear separation of test/build/package, smaller attack surface.
# ---------------------------------------------------------------------------

ARG BASE_IMAGE_RUN=eclipse-temurin:21-jre
ARG BUILD_VERSION=dev
ARG VCS_REF=local

FROM ${BASE_IMAGE_RUN} AS runtime
LABEL org.opencontainers.image.title="moapin" \
      org.opencontainers.image.description="Moapin Spring Boot service" \
      org.opencontainers.image.licenses="Apache-2.0" \
      org.opencontainers.image.source="https://example.com/repo" \
      org.opencontainers.image.revision="${VCS_REF}" \
      org.opencontainers.image.version="${BUILD_VERSION}"

ENV TZ=Asia/Seoul \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -XX:+AlwaysActAsServerClassMachine -XX:+UseG1GC -Duser.timezone=UTC"

WORKDIR /app
RUN useradd --system --create-home --uid 1001 appuser 2>/dev/null || adduser -u 1001 -S appuser

# Copy pre-built fat JAR (pattern: *SNAPSHOT.jar). Adjust if versioned differently.
COPY build/libs/*SNAPSHOT.jar app.jar

USER appuser
EXPOSE 8080

# HEALTHCHECK (enable if actuator exposed)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
#   CMD wget -qO- http://127.0.0.1:8080/actuator/health | grep '"status":"UP"' || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]


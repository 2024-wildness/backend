[English](README.md) | [한국어](README.ko.md)

# Moapin 백엔드

Spring Boot 3.5.x (Java 21 toolchain) 기반 서비스.

## 1. 빠른 시작(Quick Start)
```bash
# (개발 프로필, 인메모리 H2, Swagger 활성화)
./gradlew bootRun --args='--spring.profiles.active=dev'
```
다음 주소를 열어 확인:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 2. 실행 모드(Run Modes)
| 모드 | 사용 시점 | 명령 | 비고 |
|------|-----------|------|------|
| Gradle Dev | 빠른 반복 개발 | `./gradlew bootRun --args='--spring.profiles.active=dev'` | DevTools가 있으면 자동 재시작 |
| 실행 JAR | 로컬 프로덕션 유사 환경 | `./gradlew clean bootJar` 후 `java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev` | compose 서비스 자동 시작: `--spring.docker.compose.enabled=true` 추가 |
| Docker Compose | 전체 스택(Postgres/Redis/RabbitMQ) | `docker compose up -d` | 앱 서비스 기본 프로필: `SPRING_PROFILES_ACTIVE=prod` |
| Buildpacks | Dockerfile 없이 OCI 이미지 | `./gradlew bootBuildImage --imageName=moapin-app:pack` | Paketo buildpacks 사용 |
| Jib | 빠른 이미지 빌드 | `./gradlew jibDockerBuild -x test` | 로컬에 `moapin-app:jib` 생성 |
| Native(선택) | 시작 속도/메모리 최적화 | `./gradlew nativeCompile` | 결과 바이너리: `build/native/nativeCompile/` |

### 2.1 JAR 실행 시 Docker Compose 연동
```bash
java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar \
	--spring.profiles.active=dev \
	--spring.docker.compose.enabled=true
```
`compose.yaml`에 데이터베이스/브로커가 정의되어 있으면 자동 기동되며, 없으면 H2로 폴백됩니다.

## 3. 프로파일(Profiles)
| 프로파일 | 목적 | DB | Swagger | 비고 |
|----------|------|----|---------|------|
| dev | 로컬 개발 | H2(인메모리) | 활성화 | 빠른 부팅, 완화된 설정 |
| prod | 프로덕션/Compose | Postgres | 비활성화(예정) | 외부 서비스(Redis/RabbitMQ) |

## 4. 테스트(Tests)
```bash
./gradlew test
```
REST Docs/스니펫(활성화된 경우)은 `build/generated-snippets`에 생성됩니다.

## 5. 데이터베이스 및 마이그레이션
- Flyway가 `src/main/resources/db/migration`의 SQL(V1__*.sql 등)을 자동 적용합니다.
- 개발(dev)은 H2 사용: `jdbc:h2:mem:moapin` (실행 간 지속성 없음)
- 프로덕션/Compose는 Postgres 사용(환경 변수 참조)
- 마이그레이션 문제 복구: `./gradlew bootRun --args='--spring.flyway.repair=true'`

## 6. Swagger / OpenAPI
- 라이브러리: `springdoc-openapi-starter-webmvc-ui`
- 개발 URL: `/swagger-ui/index.html`
- Swagger Authorize에서 `Authorization: Bearer <token>` 설정(스킴: bearer-jwt)
- 프로덕션에서는 프로파일 오버라이드로 UI 비활성화 권장(추후 `application-prod.yml` 예시 추가 예정).

## 7. 보안 및 JWT
- JWT 시크릿은 RAW 또는 Base64 허용; 프로퍼티 경로: `app.security.jwt.secret`(프로젝트 설정에 따라 상이할 수 있음)
- 커스텀 인증 구성이 완전히 연결되지 않은 경우, 초기 부팅 시 Spring Boot 기본 비밀번호 로그가 보일 수 있음. 프로덕션에서는 실제 사용자/보안 구성을 사용하세요.
- 명시적으로 허용한 경우를 제외하고 Actuator 엔드포인트는 보호됨.

## 8. 아티팩트 빌드
```bash
# JAR
./gradlew clean bootJar

# OCI 이미지(Buildpacks)
./gradlew bootBuildImage --imageName=moapin-app:pack

# OCI 이미지(Jib, tar 대상 시 Docker 데몬 불필요)
./gradlew jibDockerBuild -x test

# 네이티브 바이너리
./gradlew nativeCompile
```

## 9. Makefile 단축 명령
```bash
make jar          # bootJar 빌드(테스트 생략)
make build        # jar + docker build(앱 이미지)
make jib          # Jib로 이미지 빌드
make dev          # Gradle로 개발 프로필 실행(bootRun)
make run          # 빌드된 jar 실행(없으면 자동 빌드) + dev 프로필
make smoke        # 원샷: jar 빌드 -> 기동 -> 헬스체크 -> 종료
make infra-up     # postgres + redis + rabbitmq만 시작
make infra-down   # 인프라 서비스 중지
make compose-only # 이미지 빌드 후 앱 컨테이너만 시작(동작 중 인프라 재사용)
make up           # docker compose up -d(정의된 전체 스택)
make logs         # 앱 로그 팔로우(compose 서비스)
make down         # docker compose down -v
make health       # actuator health 호출(localhost:8080)
```
서비스가 비정상이면 헬스체크 명령은 0이 아닌 종료코드를 반환합니다.

## 10. 주요 환경 변수(Compose)
| 변수 | 기본값(Compose) | 용도 |
|------|------------------|------|
| SPRING_PROFILES_ACTIVE | prod | prod 프로필 활성화 |
| SPRING_DATASOURCE_URL | jdbc:postgresql://postgres:5432/moapin | 데이터베이스 URL |
| SPRING_DATASOURCE_USERNAME | moapin | DB 사용자 |
| SPRING_DATASOURCE_PASSWORD | moapin | DB 비밀번호 |
| SPRING_REDIS_HOST | redis | Redis 호스트 |
| SPRING_RABBITMQ_HOST | rabbitmq | 브로커 호스트 |

추가(필요 시 설정):
| 용도 | 프로퍼티 |
|------|----------|
| JWT Secret | `APP_SECURITY_JWT_SECRET`(또는 매핑된 env 바인딩) |
| AWS S3 Region | `AWS_REGION` |
| Meilisearch Host | `MEILISEARCH_HOST` |

## 11. 로깅 및 트레이싱
- MDC 키: `requestId`, `userId`
- 들어온 `X-Request-Id`를 재사용하거나 생성
- 중앙집중 로깅을 위한 JSON 레이아웃 옵션 고려(향후 개선 항목)

## 12. 문제 해결(Troubleshooting)
| 증상 | 원인 추정 | 해결책 |
|------|-----------|--------|
| Flyway 검증 오류 | 과거 마이그레이션 변경 | 기존 파일 수정 대신 새 마이그레이션 작성; 체크섬 드리프트가 의도된 경우에만 `repair` 사용 |
| H2 vs Postgres 불일치 | 잘못된 프로파일 | `--spring.profiles.active=prod` 전달 또는 compose 환경 재설정 |
| Swagger 404 | 프로파일에서 UI 비활성화 | dev 프로필 사용 또는 `springdoc.api-docs.enabled=true` 속성 활성화 |
| `/actuator/health` 401 | Spring Security 보호 | `SecurityFilterChain`에서 허용하거나 인증 헤더 사용 |
| 기본 비밀번호 로그 출력 | 기본 사용자 생성 | 실제 보안 구성 제공/커스텀 인증 빈들이 정상 로드되는지 확인 |
| 8080 포트 점유 | 다른 프로세스 실행 중 | `lsof -i :8080` 후 프로세스 종료 또는 `server.port` 변경 |

## 13. 명령어 치트시트
```bash
# 개발 실행
./gradlew bootRun --args='--spring.profiles.active=dev'

# JAR 실행(dev)
java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# JAR + compose 연동
java -jar build/libs/moapin-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --spring.docker.compose.enabled=true

# 전체 스택 기동
docker compose up -d

# 스택 중지
docker compose down -v

# Makefile 빠른 흐름
make smoke        # 빠른 로컬 스모크 검증
make infra-up && make run  # 인프라 후 앱 직접 실행
make infra-down   # 인프라만 종료
make build && docker run --rm -p 8080:8080 moapin-app:dev
```

## 15. CI/CD 워크플로
| 워크플로 | 경로 | 트리거 | 목적 | 주요 단계 |
|---------|------|--------|------|----------|
| Gradle Validate | `.github/workflows/ci-gradle.yml` | PR(dev), push(feature/**), 수동 | 품질 게이트(컴파일, 정적 검사, 테스트) | Checkstyle, tests, bootJar, Trivy(fs) |
| Internal Fast Smoke | `.github/workflows/internal-smoke.yml` | PR(dev/main), push(feature/**) | 초고속 실행 헬스 스모크 | compile → bootJar → run(dev) → /actuator/health |
| Docker Build & Push | `.github/workflows/docker-build-push.yml` | push(main/dev), 수동 | 런타임 이미지 빌드 및(선택) 푸시 + 보안 아티팩트 | bootJar, buildx, 태그, Trivy 이미지 스캔, SBOM |
| Release Tag Helper | `.github/workflows/release-tag.yml` | 수동(dispatch) | 주석 태그 생성 및(선택) 이미지 빌드 트리거 | git tag, 선택적 dispatch |

### 15.1 이미지 태깅 전략
- `latest` → main 브랜치
- `dev` → dev 브랜치
- `<12char sha>` 커밋별 기본 태그
- 수동 워크플로의 `imageTag` 입력 또는 Release Tag Helper(`vX.Y.Z`)로 커스텀 태그 지정

### 15.2 SBOM 및 취약점
- SBOM(SPDX JSON)은 `sbom` 아티팩트로 업로드됨
- 현재 Trivy 스캔(파일시스템 + 이미지)은 빌드 실패를 유발하지 않음. 이후 HIGH/CRITICAL에서 실패하도록 강제하려면 `exit-code: 1`, `severity: HIGH,CRITICAL` 설정.

### 15.3 빠른 검증 vs 전체 검증
| 레이어 | 대상 | 테스트 포함 | 보안 스캔 | 이미지 빌드 |
|--------|------|-------------|-----------|-------------|
| Smoke | internal-smoke | 아니오 | 아니오 | 아니오 |
| Validate | ci-gradle | 예 | 파일시스템(Trivy) | Jar만 |
| Image | docker-build-push | (테스트 생략) | 이미지(Trivy) + SBOM | 예 |

### 15.4 로컬 재현
```bash
# 내부 스모크 워크플로와 동일하게 실행
./gradlew compileJava -x checkstyleMain -x checkstyleTest
./gradlew bootJar -x test
SPRING_PROFILES_ACTIVE=dev java -jar build/libs/*SNAPSHOT.jar &
curl -fsS --retry 20 --retry-delay 2 http://localhost:8080/actuator/health
kill %1
```

## 16. 로드맵 / 향후 개선 사항
- JSON 로깅 앱렌더 옵션
- 보안/비밀 관리 강화(Vault / AWS Secrets Manager)
- 가시성(Observability): 메트릭 대시보드, 트레이싱 익스포터
- 프로덕션에서 Swagger 접근 제한 또는 제거
- Trivy 심각도 게이트(HIGH/CRITICAL) 적용
- integrationTest 태스크 추가 및 선택적 CI 경로 승격
- Distroless / 멀티아키 이미지 빌드(buildx)

---
내부 Gemini CLI 워크플로 가이드라인을 따라 유지 관리됨(`.github/instructions/gemini_cli.instructions.md` 참조).


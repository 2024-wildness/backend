# [EPIC] 도메인 Auditing & Flyway 1차 정비 완료 및 후속 작업 트래킹

## 1. 개요 (What)
Flyway 기반 스키마 버전 관리 도입과 함께 핵심 도메인(User / Category / Content)에 공통 감사 필드(Auditable: created_at, updated_at)를 적용하여 일관된 시간 필드 전략을 확립했습니다. User 도메인 확장(status, lastLoginAt) 및 Category 고유 제약(사용자별 이름 중복 방지)을 추가했습니다.

## 2. 적용 배경 (Why)
| 문제 | 영향 | 해결 |
|------|------|------|
| 비버전 DDL (자동 생성) | 이력 추적 불가 / 배포 리스크 | Flyway baseline + 마이그레이션 적용 |
| 엔티티별 상이한 타임스탬프 전략 | 감사/정합성 저하 | Auditable 추상화 통일 |
| Category 이름 중복 허용 | UX 저하 / 비즈 무결성 약화 | (user_id, name) UNIQUE |
| User 상태/마지막 로그인 미존재 | 운영 모니터링 한계 | status / lastLoginAt 필드 추가 |
| 향후 Content 다형성 확장 대비 부족 | 상속 전략 재검토 필요 | 사전 정리 및 표준 타임스탬프 도입 |

## 3. 주요 변경 (Diff Highlights)
### 마이그레이션
- `V1__baseline.sql`: 베이스라인 표시 (빈/최소)
- `V2__create_users_and_categories.sql`: users, categories 테이블 + 인덱스/유니크/외래키

### 코드/구조
- User / Category / Content → `Auditable` 상속 (created_at, updated_at)
- User: `status (enum ACTIVE/INACTIVE)`, `lastLoginAt`, 인덱스 추가
- Category: `(user_id, name)` UNIQUE + cascade remove 제거 (콘텐츠 보존 전략 준비)
- Content: Hibernate timestamp → Auditable 전환
- Flyway 설정 추가 (`baseline-on-migrate`, `clean-disabled`)
- Auditing 통합 테스트 정비 (한글 주석화)
- Legacy 패키지(기존 auth/category/content 등) 제거 및 신규 패키지 구조 재구성

## 4. 데이터 마이그레이션 / 적용 방법
```bash
# 개발 환경 적용 예시
./gradlew bootRun
# 또는
SPRING_PROFILES_ACTIVE=dev ./gradlew flywayMigrate
```
- `flyway_schema_history` 테이블에서 V1, V2 적용 여부 확인
- 기존 DB가 이미 테이블을 갖고 있었다면 baseline 충돌 여부(컬럼 차이) 수동 점검 필요

### 롤백(초기 단계 가정)
- 아직 운영 반영 전이라면: 브랜치 revert
- 운영 반영 후라면: DROP 지양, 수정 필요 시 추가 보정 마이그레이션(V3+) 작성

## 5. 검증 사항
| 항목 | 결과 |
|------|------|
| 컴파일 | OK |
| Auditing 테스트 | created_at/updated_at 생성·갱신 검증 OK |
| 마이그레이션 구문 | PostgreSQL 기준 수기 리뷰 OK |
| User 기본 status | ACTIVE 기본 값 설정 확인 |

## 6. Breaking Changes
- 구(legacy) 컨트롤러/엔티티 삭제 → 외부 의존 경로/프론트엔드 호출부 수정 필요

## 7. 리스크 & 대응
| 리스크 | 설명 | 대응 |
|--------|------|------|
| Category 삭제 시 Content 처리 미정 | orphan (FK NULL) 전략 미구현 | 후속 작업에서 null 처리 + 테스트 |
| User 삭제 cascade 영향 | 연쇄 삭제/데이터 손실 우려 | 삭제 정책 명시 전 운영 삭제 제한 |
| Content 상속 전략 미확정 | 뒤늦은 JOINED 전환 비용 증가 | 빠른 결정(현 EPIC 후속) |
| password 컬럼 네이밍 | 해시 의미 불명확 | `password_hash` 리네이밍 여부 후속 판단 |

## 8. 후속 작업 (체크리스트)
- [ ] V3 마이그레이션: Content 스키마/인덱스 (user_id, category_id, pinned, created_at DESC 등)
- [ ] Category 삭제 로직: 관련 Content.category NULL 처리 + 서비스/테스트
- [ ] 로그인 성공 시 User.lastLoginAt 갱신 (Security Filter/Listener)
- [ ] (선택) password → password_hash 컬럼명 변경 및 마이그레이션
- [ ] Content 상속 전략 확정 (SINGLE_TABLE 유지 vs JOINED) 및 문서화
- [ ] 글로벌 예외 처리 계층 도입 (@ControllerAdvice + 커스텀 예외)
- [ ] User 삭제 정책 정의 (CASCADE, soft delete, 익명화 중 결정)
- [ ] 추가 테스트: 유니크 위반, 삭제 정책, Content auditing
- [ ] README / 도메인 & 스키마 문서 업데이트

## 9. 리뷰어 체크포인트
- [ ] Auditable 미적용 누락 엔티티 없음
- [ ] 마이그레이션 네이밍/순서 적절
- [ ] Category UNIQUE 제약 비즈니스 규칙 충족
- [ ] User status / lastLoginAt 모델링 수용 가능
- [ ] Breaking 제거 목록 타당 & 커뮤니케이션 충분
- [ ] 후속 작업 범위 명확

## 10. 기타 메모
- 운영 반영 전 Content 상속 전략 확정 권장
- README에 "Schema & Auditing Policy" 섹션 추가 예정

---
이 이슈는 EPIC 이며 체크리스트 완료 후 Close 예정.

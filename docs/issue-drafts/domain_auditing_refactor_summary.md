# 도메인 / Auditing / Flyway 1차 정비 요약

## 완료 사항
- Flyway 도입: V1 baseline, V2 users & categories
- Auditable 상속 통일: User / Category / Content
- User: status(enum), lastLoginAt, 인덱스(email/username)
- Category: (user_id, name) UNIQUE + 인덱스, cascade 제거
- Content: Hibernate timestamp -> Auditable
- Auditing 테스트 한글 정리
- application.yml Flyway 설정 (baseline-on-migrate, clean-disabled)

## Breaking Changes
- 구 패키지 구조의 다수 컨트롤러/엔티티 삭제 → 의존 코드 수정 필요

## 남은 TODO (후속 이슈 전환 예정)
1. V3 마이그레이션(Content 스키마 및 인덱스)
2. Category 삭제 시 Content.category NULL 처리 로직 & 테스트
3. 로그인 성공 시 User.lastLoginAt 갱신 로직
4. (선택) password -> password_hash 리네이밍 마이그레이션
5. Content 상속 전략 재검토 (SINGLE_TABLE 유지 vs JOINED) 문서화
6. 글로벌 예외 처리 (@ControllerAdvice, 커스텀 예외)
7. Prod DB / Flyway 프로파일 문서화
8. User 삭제 정책 및 Category/Content 후속 처리 정의
9. 테스트 추가 (유니크 제약, auditing, 삭제 정책)
10. README / 도메인 모델 문서 업데이트

## 위험 / 의사결정 필요
- Content 상속 전략 변경 시점 (조기 확정 권장)
- User 삭제 정책 미정 (CASCADE 유지 여부)

## 커밋 메시지 제안
```
feat(domain): introduce Flyway baseline & unify auditing across core entities

- Add Flyway (V1 baseline, V2 users/categories)
- Refactor User/Category/Content to extend Auditable
- User: status, lastLoginAt, indexes
- Category: unique (user_id,name), remove cascade
- Content: auditing unification

BREAKING CHANGE: legacy controllers/entities removed.
```

## PR 본문 템플릿 요약
```
## 변경 개요
Auditing/Flyway 기반 통일 및 User/Category 구조 강화.

## 주요 변경
(위 커밋 요약 재사용)

## Breaking Changes
구 패키지 엔티티 제거.

## 검증
- 컴파일 성공
- Auditing 테스트 통과
- SQL 문법 검토 (PostgreSQL 기준)

## 후속 작업
TODO 항목 체크박스 나열
```

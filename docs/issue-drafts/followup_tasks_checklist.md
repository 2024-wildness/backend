# 후속 작업 체크리스트

- [ ] V3__content_schema 마이그레이션 (content 테이블/인덱스)
- [ ] Category 삭제 로직: 관련 Content.category NULL 처리 + 테스트
- [ ] User 로그인 성공 시 lastLoginAt 업데이트 로직(Security 필터/Listener)
- [ ] (선택) password -> password_hash 컬럼명 변경 (마이그레이션 + 코드)
- [ ] Content 상속 전략 확정 및 문서화 (SINGLE_TABLE vs JOINED)
- [ ] 글로벌 예외 처리 계층 도입 (@ControllerAdvice + 커스텀 예외)
- [ ] Prod 환경 DB/Flyway 설정 문서화 (application-prod.yml 예시 포함)
- [ ] User 삭제 정책 정의 및 구현 (CASCADE 유지/삭제/마스킹 중 선택)
- [ ] 추가 테스트: 유니크 제약, auditing update, 삭제 정책, Content auditing
- [ ] README / 아키텍처 문서 업데이트

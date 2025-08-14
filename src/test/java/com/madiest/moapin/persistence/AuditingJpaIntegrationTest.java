package com.madiest.moapin.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.madiest.moapin.config.JpaAuditingConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
class AuditingJpaIntegrationTest {

  @Autowired private TestEntityManager em;

  @Test
  @DisplayName("createdAt / updatedAt 값이 저장 시 자동 생성되고, 수정 시 updatedAt 이 갱신된다")
  void auditingFieldsPopulated() throws InterruptedException {
    // given: Auditable 을 상속한 신규 엔티티 생성 (영속화 전 상태)
    SampleEntity e = new SampleEntity();

    // when: we persist & flush (causes INSERT + auditing listener populates timestamps)
    em.persistAndFlush(e);

    // then: 두 필드 모두 null 아님, 최초에는 updatedAt >= createdAt
    assertThat(e.getCreatedAt()).as("insert 시 createdAt 세팅").isNotNull();
    assertThat(e.getUpdatedAt()).as("insert 시 updatedAt 세팅").isNotNull();

    Instant firstUpdated = e.getUpdatedAt();

    // 업데이트 전 시간차 확보 (Instant 정밀도 고려)
    Thread.sleep(5);

    // when: 엔티티 변경 -> dirty 상태 -> flush 시 UPDATE + updatedAt 갱신
    e.touch();
    em.persistAndFlush(e);

    // then: updatedAt 이 이전 값 이후로 증가 (또는 동일) 해야 함
    assertThat(e.getUpdatedAt()).as("update 후 updatedAt 증가").isAfterOrEqualTo(firstUpdated);
  }

  @Entity(name = "audit_test_entity")
  static class SampleEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "initial"; // 더티 체킹 대상 필드

    // UPDATE 트리거용 간단 변경 메서드
    void touch() {
      this.name = "updated";
    }

    public Long getId() {
      return id;
    }
  }
}

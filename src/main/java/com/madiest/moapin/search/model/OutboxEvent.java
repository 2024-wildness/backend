package com.madiest.moapin.search.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long contentId;

  @Enumerated(EnumType.STRING)
  private Operation operation;

  @Lob private String payload;

  private boolean processed;

  public enum Operation {
    CREATE,
    UPDATE,
    DELETE
  }

  public OutboxEvent(Long contentId, Operation operation, String payload) {
    this.contentId = contentId;
    this.operation = operation;
    this.payload = payload;
    this.processed = false;
  }

  public void setProcessed(boolean processed) {
    this.processed = processed;
  }
}

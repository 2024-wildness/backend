package com.madiest.moapin.auth.model;

import com.madiest.moapin.persistence.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Core User entity. Extends Auditable for createdAt / updatedAt timestamps. NOTE: Password field
 * stores a bcrypt/argon2 hash (value naming kept as 'password' for backward compatibility).
 */
@Getter
@Setter
@Entity
@Table(
    name = "users",
    indexes = {
      @Index(name = "idx_users_email", columnList = "email"),
      @Index(name = "idx_users_username", columnList = "username")
    })
public class User extends Auditable {

  public enum Status {
    ACTIVE,
    INACTIVE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String username;

  @Column(unique = true, nullable = false, length = 320)
  private String email;

  @Column(nullable = false, length = 255)
  private String password; // hashed password

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private Status status = Status.ACTIVE;

  @Column(name = "last_login_at")
  private Instant lastLoginAt;

}

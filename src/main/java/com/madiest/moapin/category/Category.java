package com.madiest.moapin.category;

import com.madiest.moapin.auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a user-defined category.
 */
@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 엔티티가 처음 저장되기 전에 생성 시각을 현재 시각으로 설정합니다.
     */
    @PrePersist
    void onCreate() {
        this.createdAt = java.time.Instant.now();
    }
}
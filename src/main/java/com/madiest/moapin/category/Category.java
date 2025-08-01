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

    @PrePersist
    void onCreate() {
        this.createdAt = java.time.Instant.now();
    }
}
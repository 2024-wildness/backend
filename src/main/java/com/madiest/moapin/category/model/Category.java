package com.madiest.moapin.category.model;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.persistence.Auditable;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_category_user_name", columnNames = {"user_id", "name"})
       },
       indexes = {
           @Index(name = "idx_category_user", columnList = "user_id"),
           @Index(name = "idx_category_user_name", columnList = "user_id,name")
       })
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_index")
    private Integer orderIndex;

    // Removed cascade REMOVE + orphanRemoval to satisfy spec: content items should be retained (category set null) when category removed.
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Content> contents = new ArrayList<>();

    public Category() {}

    public Category(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public List<Content> getContents() { return contents; }
    public void setContents(List<Content> contents) { this.contents = contents; }
}

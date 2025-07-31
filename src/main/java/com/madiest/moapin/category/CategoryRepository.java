package com.madiest.moapin.category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Category database operations.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
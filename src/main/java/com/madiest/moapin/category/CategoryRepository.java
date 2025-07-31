package com.madiest.moapin.category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Category database operations.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find categories for a user sorted by the given Sort.
     */
    java.util.List<Category> findByUser(com.madiest.moapin.auth.User user, org.springframework.data.domain.Sort sort);
}
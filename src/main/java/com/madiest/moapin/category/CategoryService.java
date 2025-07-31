package com.madiest.moapin.category;

import com.madiest.moapin.auth.User;
import com.madiest.moapin.auth.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing categories.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new category for the authenticated user.
     */
    @Transactional
    public Category createCategory(Category category, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        category.setUser(user);
        return categoryRepository.save(category);
    }

    /**
     * Retrieve a category if it belongs to the authenticated user.
     */
    @Transactional(readOnly = true)
    public Category getCategory(Long id, Authentication auth) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Category not found"));
        if (!category.getUser().getUsername().equals(auth.getName())) {
            throw new IllegalStateException("Access denied");
        }
        return category;
    }
}
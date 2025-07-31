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
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));
        category.setUser(user);
        return categoryRepository.save(category);
    }

    /**
     * Retrieve a category if it belongs to the authenticated user.
     */
    @Transactional(readOnly = true)
    public Category getCategory(Long id, Authentication auth) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Category not found"));
        if (!category.getUser().getUsername().equals(auth.getName())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Access denied");
        }
        return category;
    }

    @Transactional(readOnly = true)
    public java.util.List<com.madiest.moapin.category.payload.CategoryListResponse> listCategories(
            Authentication auth, String sort) {
        com.madiest.moapin.auth.User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        org.springframework.data.domain.Sort sortOrder;
        switch (sort) {
            case "name":
                sortOrder = org.springframework.data.domain.Sort.by("name");
                break;
            case "custom":
                sortOrder = org.springframework.data.domain.Sort.by("orderIndex");
                break;
            case "createdDate":
            default:
                sortOrder = org.springframework.data.domain.Sort.by("createdAt");
        }
        java.util.List<Category> categories =
                categoryRepository.findByUser(user, sortOrder);
        java.util.List<com.madiest.moapin.category.payload.CategoryListResponse> result = new java.util.ArrayList<>();
        for (Category cat : categories) {
            result.add(new com.madiest.moapin.category.payload.CategoryListResponse(
                    cat.getId(),
                    cat.getName(),
                    cat.getOrderIndex(),
                    cat.getCreatedAt(),
                    0L
            ));
        }
        return result;
    }

    /**
     * Update the name of an existing category belonging to the authenticated user.
     */
    @Transactional
    public Category updateCategory(Long id, Category update, Authentication auth) {
        Category category = getCategory(id, auth);
        category.setName(update.getName());
        return categoryRepository.save(category);
    }

    /**
     * Reorder categories by updating their orderIndex according to the given sequence of IDs.
     */
    @Transactional
    public void reorderCategories(java.util.List<Long> orderedIds, Authentication auth) {
        for (int i = 0; i < orderedIds.size(); i++) {
            Long catId = orderedIds.get(i);
            Category category = getCategory(catId, auth);
            category.setOrderIndex(i);
            categoryRepository.save(category);
        }
    }

    /**
     * Delete a category and cascade removal of associated content.
     */
    @Transactional
    public void deleteCategory(Long id, Authentication auth) {
        Category category = getCategory(id, auth);
        categoryRepository.delete(category);
    }
}
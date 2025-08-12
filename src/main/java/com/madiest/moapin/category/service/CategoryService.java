package com.madiest.moapin.category.service;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(String name) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = new Category(name, user);
        // TODO: Set initial order_index
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long categoryId, String name) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        // TODO: Verify cascade deletion of contents is handled
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories(String sortBy, Sort.Direction direction) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sort sort = Sort.by(direction, sortBy);
        return categoryRepository.findAllByUser(user, sort);
    }

    @Transactional
    public void updateCategoryOrder(List<Long> categoryIds) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> categories = categoryRepository.findAllByUser(user);
        for (int i = 0; i < categoryIds.size(); i++) {
            long categoryId = categoryIds.get(i);
            int finalI = i;
            categories.stream()
                    .filter(c -> c.getId().equals(categoryId))
                    .findFirst()
                    .ifPresent(c -> c.setOrderIndex(finalI));
        }
        categoryRepository.saveAll(categories);
    }
}

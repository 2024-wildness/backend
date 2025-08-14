package com.madiest.moapin.category.service;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.auth.repository.UserRepository;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  private User getCurrentUser() {
    String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found: " + username));
  }

  @Transactional
  public Category createCategory(String name) {
    User user = getCurrentUser();
    Category category = new Category(name, user);
    // TODO: Set initial order_index
    return categoryRepository.save(category);
  }

  @Transactional
  public Category updateCategory(Long categoryId, String name) {
    User user = getCurrentUser();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new RuntimeException("Category not found"));
    
    if (!category.getUser().getId().equals(user.getId())) {
      throw new AccessDeniedException("Access denied to category");
    }
    
    category.setName(name);
    return categoryRepository.save(category);
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    User user = getCurrentUser();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new RuntimeException("Category not found"));
    
    if (!category.getUser().getId().equals(user.getId())) {
      throw new AccessDeniedException("Access denied to category");
    }
    
    // TODO: Verify cascade deletion of contents is handled
    categoryRepository.delete(category);
  }

  @Transactional(readOnly = true)
  public List<Category> getCategories(String sortBy, Sort.Direction direction) {
    User user = getCurrentUser();
    Sort sort = Sort.by(direction, sortBy);
    return categoryRepository.findAllByUser(user, sort);
  }

  @Transactional(readOnly = true)
  public Category getCategory(Long categoryId) {
    User user = getCurrentUser();
    return categoryRepository
        .findByIdAndUser(categoryId, user)
        .orElseThrow(() -> new RuntimeException("Category not found"));
  }

  @Transactional
  public void updateCategoryOrder(List<Long> categoryIds) {
    User user = getCurrentUser();
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

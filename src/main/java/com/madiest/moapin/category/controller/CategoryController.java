package com.madiest.moapin.category.controller;

import com.madiest.moapin.category.dto.CategoryCreateRequest;
import com.madiest.moapin.category.dto.CategoryListResponse;
import com.madiest.moapin.category.dto.CategoryUpdateRequest;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryCreateRequest request) {
        Category category = categoryService.createCategory(request.getName());
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequest request) {
        Category category = categoryService.updateCategory(categoryId, request.getName());
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> getCategories(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        List<Category> categories = categoryService.getCategories(sortBy, direction);
        List<CategoryListResponse> response = categories.stream().map(CategoryListResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/order")
    public ResponseEntity<Void> updateCategoryOrder(@RequestBody List<Long> categoryIds) {
        categoryService.updateCategoryOrder(categoryIds);
        return ResponseEntity.ok().build();
    }
}

package com.madiest.moapin.category;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST endpoints for category management.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody Category category,
            Authentication auth) {
        Category saved = categoryService.createCategory(category, auth);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(
            @PathVariable Long id,
            Authentication auth) {
        Category cat = categoryService.getCategory(id, auth);
        return ResponseEntity.ok(cat);
    }

    @GetMapping
    public ResponseEntity<java.util.List<com.madiest.moapin.category.payload.CategoryListResponse>> listCategories(
            org.springframework.web.bind.annotation.RequestParam(value = "sort", defaultValue = "createdDate") String sort,
            Authentication auth) {
        java.util.List<com.madiest.moapin.category.payload.CategoryListResponse> list =
                categoryService.listCategories(auth, sort);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody Category update,
            Authentication auth) {
        Category updated = categoryService.updateCategory(id, update, auth);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/reorder")
    public ResponseEntity<Void> reorderCategories(
            @Valid @RequestBody java.util.List<Long> orderedIds,
            Authentication auth) {
        categoryService.reorderCategories(orderedIds, auth);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            Authentication auth) {
        categoryService.deleteCategory(id, auth);
        return ResponseEntity.noContent().build();
    }
}
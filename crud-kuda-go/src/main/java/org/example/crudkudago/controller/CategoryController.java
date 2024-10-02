package org.example.crudkudago.controller;

import org.example.crudkudago.entity.Category;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface CategoryController {
    @GetMapping
    List<Category> getAllCategories();

    @GetMapping("/{id}")
    Category getCategoryById(@PathVariable UUID id);

    @PostMapping
    void createCategory(@RequestBody Category category);

    @PutMapping("/{id}")
    void updateCategory(@PathVariable UUID id, @RequestBody Category category);

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable UUID id);
}

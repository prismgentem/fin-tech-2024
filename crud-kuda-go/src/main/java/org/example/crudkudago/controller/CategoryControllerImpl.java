package org.example.crudkudago.controller;

import lombok.RequiredArgsConstructor;
import org.example.crudkudago.annotation.LogExecutionTime;
import org.example.crudkudago.entity.Category;

import org.example.crudkudago.service.CategoryServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@LogExecutionTime
@RestController
@RequestMapping("/api/v1/places/categories")
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {
    private final CategoryServiceImpl categoryService;
    @Override
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryService.getCategoryById(id);
    }

    @Override
    public void createCategory(Category category) {
        categoryService.createCategory(category);
    }

    @Override
    public void updateCategory(UUID id, Category category) {
        categoryService.updateCategory(id, category);
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryService.deleteCategory(id);
    }
}

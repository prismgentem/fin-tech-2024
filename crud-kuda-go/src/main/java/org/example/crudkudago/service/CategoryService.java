package org.example.crudkudago.service;

import org.example.crudkudago.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(UUID id);

    void createCategory(Category category);

    void updateCategory(UUID id, Category category);

    void deleteCategory(UUID id);
}

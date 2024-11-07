//package org.example.crudkudago.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.crudkudago.exception.CategoryNotFoundException;
//import org.example.crudkudago.exception.CategoryOperationException;
//import org.example.crudkudago.entity.Category;
//import org.example.crudkudago.repository.InMemoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class CategoryServiceImpl implements CategoryService {
//    private final InMemoryRepository<Category> categoryRepository;
//
//    @Autowired
//    public CategoryServiceImpl(InMemoryRepository<Category> categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    @Override
//    public List<Category> getAllCategories() {
//        return List.copyOf(categoryRepository.findAll().values());
//    }
//
//    @Override
//    public Category getCategoryById(UUID id) {
//        Category category = categoryRepository.findById(id);
//        if (category == null) {
//            log.warn("Category with ID {} not found", id);
//            throw new CategoryNotFoundException("Category with ID " + id + " not found");
//        }
//        return category;
//    }
//
//    @Override
//    public void createCategory(Category category) {
//        try {
//            var id = UUID.randomUUID();
//            category.setId(id);
//            categoryRepository.save(id, category);
//        } catch (Exception e) {
//            log.error("Error while creating category", e);
//            throw new CategoryOperationException("Failed to create category", e);
//        }
//    }
//
//    @Override
//    public void updateCategory(UUID id, Category category) {
//        if (categoryRepository.findById(id) == null) {
//            log.warn("Category with ID {} not found for update", id);
//            throw new CategoryNotFoundException("Category with ID " + id + " not found");
//        }
//        try {
//            categoryRepository.update(id, category);
//        } catch (Exception e) {
//            log.error("Error while updating category with ID {}", id, e);
//            throw new CategoryOperationException("Failed to update category", e);
//        }
//    }
//
//    @Override
//    public void deleteCategory(UUID id) {
//        if (categoryRepository.findById(id) == null) {
//            log.warn("Category with ID {} not found for deletion", id);
//            throw new CategoryNotFoundException("Category with ID " + id + " not found");
//        }
//        try {
//            categoryRepository.delete(id);
//        } catch (Exception e) {
//            log.error("Error while deleting category with ID {}", id, e);
//            throw new CategoryOperationException("Failed to delete category", e);
//        }
//    }
//}
//

package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.entity.Category;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private static final String MSG_CATEGORY_NOT_FOUND = "Category with id '%s' not found";
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_CATEGORY_NOT_FOUND, id));
    }

    public Mono<Void> createCategory(Category category) {
        return Mono.fromRunnable(() ->
                categoryRepository.save(category)
        );
    }

    public void updateCategory(UUID id, Category category) {
        categoryRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_CATEGORY_NOT_FOUND, id));
        categoryRepository.save(category);
    }

    public void deleteCategoryById(UUID id) {
        categoryRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_CATEGORY_NOT_FOUND, id));
        categoryRepository.deleteById(id);
    }
}


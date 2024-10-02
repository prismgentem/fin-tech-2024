package org.example.crudkudago.unit;

import org.example.crudkudago.entity.Category;
import org.example.crudkudago.exception.CategoryNotFoundException;
import org.example.crudkudago.exception.CategoryOperationException;
import org.example.crudkudago.repository.InMemoryRepository;
import org.example.crudkudago.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {

    @Mock
    private InMemoryRepository<Category> categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category(UUID.randomUUID(), "slug-test", "Test Category");
    }

    @Test
    void getAllCategoriesSuccess() {
        Map<UUID, Category> categoryMap = new HashMap<>();
        categoryMap.put(category.getId(), category);

        when(categoryRepository.findAll()).thenReturn(categoryMap);

        List<Category> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(category.getId(), categories.get(0).getId());
    }

    @Test
    void getCategoryByIdSuccess() {
        when(categoryRepository.findById(category.getId())).thenReturn(category);

        Category foundCategory = categoryService.getCategoryById(category.getId());

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
    }

    @Test
    void getCategoryByIdNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(categoryRepository.findById(invalidId)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategoryById(invalidId);
        });
    }

    @Test
    void createCategorySuccess() {
        doNothing().when(categoryRepository).save(any(UUID.class), any(Category.class));

        assertDoesNotThrow(() -> categoryService.createCategory(category));
        verify(categoryRepository, times(1)).save(any(UUID.class), eq(category));
    }

    @Test
    void createCategoryException() {
        doThrow(new RuntimeException("Mock exception")).when(categoryRepository).save(any(UUID.class), any(Category.class));

        assertThrows(CategoryOperationException.class, () -> {
            categoryService.createCategory(category);
        });
    }

    @Test
    void updateCategorySuccess() {
        when(categoryRepository.findById(category.getId())).thenReturn(category);
        doNothing().when(categoryRepository).update(category.getId(), category);

        assertDoesNotThrow(() -> categoryService.updateCategory(category.getId(), category));
        verify(categoryRepository, times(1)).update(category.getId(), category);
    }

    @Test
    void updateCategoryNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(categoryRepository.findById(invalidId)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.updateCategory(invalidId, category);
        });
    }

    @Test
    void updateCategoryException() {
        when(categoryRepository.findById(category.getId())).thenReturn(category);
        doThrow(new RuntimeException("Mock exception")).when(categoryRepository).update(category.getId(), category);

        assertThrows(CategoryOperationException.class, () -> {
            categoryService.updateCategory(category.getId(), category);
        });
    }

    @Test
    void deleteCategorySuccess() {
        when(categoryRepository.findById(category.getId())).thenReturn(category);
        doNothing().when(categoryRepository).delete(category.getId());

        assertDoesNotThrow(() -> categoryService.deleteCategory(category.getId()));
        verify(categoryRepository, times(1)).delete(category.getId());
    }

    @Test
    void deleteCategoryNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(categoryRepository.findById(invalidId)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(invalidId);
        });
    }

    @Test
    void deleteCategoryException() {
        when(categoryRepository.findById(category.getId())).thenReturn(category);
        doThrow(new RuntimeException("Mock exception")).when(categoryRepository).delete(category.getId());

        assertThrows(CategoryOperationException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });
    }
}

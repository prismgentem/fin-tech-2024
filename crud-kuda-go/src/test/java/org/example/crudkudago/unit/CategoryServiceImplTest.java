package org.example.crudkudago.unit;

import org.example.crudkudago.entity.Category;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.repository.CategoryRepository;
import org.example.crudkudago.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategoriesSuccess() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        Map<UUID, Category> categoryMap = new HashMap<>();
        categoryMap.put(category.getId(), category);

        when(categoryRepository.findAll()).thenReturn(List.copyOf(categoryMap.values()));

        List<Category> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(category.getId(), categories.get(0).getId());
    }

    @Test
    void getCategoryByIdSuccess() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(category.getId());

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
    }

    @Test
    void getCategoryByIdNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        var ex = assertThrows(ServiceException.class, () -> {
            categoryService.getCategoryById(invalidId);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void createCategorySuccess() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        doNothing().when(categoryRepository).save(eq(category));

        assertDoesNotThrow(() -> categoryService.createCategory(category));
        verify(categoryRepository, times(1)).save(eq(category));
    }

    @Test
    void createCategoryException() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        doThrow(new ServiceException(ErrorType.NOT_FOUND,String.format("Category with id '%s' not found", category.getId()))).when(categoryRepository).save(eq(category));

        var ex = assertThrows(ServiceException.class, () -> {
            categoryService.createCategory(category);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void updateCategorySuccess() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).save(eq(category));

        assertDoesNotThrow(() -> categoryService.updateCategory(category.getId(), category));
        verify(categoryRepository, times(1)).save(eq(category));
    }

    @Test
    void updateCategoryNotFound() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        UUID invalidId = UUID.randomUUID();

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        var ex = assertThrows(ServiceException.class, () -> {
            categoryService.updateCategory(invalidId, category);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void updateCategoryException() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        doThrow(new ServiceException(ErrorType.NOT_FOUND,String.format("Category with id '%s' not found", category.getId()))).when(categoryRepository).save(eq(category));

        var ex = assertThrows(ServiceException.class, () -> {
            categoryService.updateCategory(category.getId(), category);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void deleteCategorySuccess() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(category.getId());

        assertDoesNotThrow(() -> categoryService.deleteCategoryById(category.getId()));
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    void deleteCategoryNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        var ex = assertThrows(ServiceException.class, () -> {
            categoryService.deleteCategoryById(invalidId);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void deleteCategoryException() {
        var category = new Category(UUID.randomUUID(), "slug-test", "Test Category");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        doThrow(new ServiceException(ErrorType.NOT_FOUND, String.format("Category with id '%s' not found", category.getId()))).when(categoryRepository).deleteById(category.getId());

        var ex = assertThrows(ServiceException.class, () -> {
            categoryService.deleteCategoryById(category.getId());
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }
}


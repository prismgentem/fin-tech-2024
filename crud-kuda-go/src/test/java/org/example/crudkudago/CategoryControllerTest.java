package org.example.crudkudago;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crudkudago.controller.CategoryController;
import org.example.crudkudago.entity.Category;
import org.example.crudkudago.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(category));
        var mvcResult = mockMvc.perform(get("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var categories = objectMapper.readValue(jsonResponse, Category[].class);
        assertThat(categories).hasSize(1);
        assertThat(categories[0].getName()).isEqualTo("Test Category");

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void shouldReturnCategoryById() throws Exception {
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        var mvcResult = mockMvc.perform(get("/api/v1/places/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var responseCategory = objectMapper.readValue(jsonResponse, Category.class);
        assertThat(responseCategory.getName()).isEqualTo("Test Category");
        assertThat(responseCategory.getId()).isEqualTo(categoryId);

        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    @DisplayName("POST /api/v1/places/categories - Should create new category")
    void shouldCreateCategory() throws Exception {
        var newCategory = new Category();
        newCategory.setName("New Category");
        var categoryJson = objectMapper.writeValueAsString(newCategory);

        doNothing().when(categoryService).createCategory(any(Category.class));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        var updatedCategory = new Category();
        updatedCategory.setName("Updated Category");
        var categoryJson = objectMapper.writeValueAsString(updatedCategory);

        doNothing().when(categoryService).updateCategory(any(UUID.class), any(Category.class));

        var mvcResult = mockMvc.perform(put("/api/v1/places/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(categoryService, times(1)).updateCategory(any(UUID.class), any(Category.class));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategoryById(categoryId);

        var mvcResult = mockMvc.perform(delete("/api/v1/places/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(categoryService, times(1)).deleteCategoryById(categoryId);
    }
}


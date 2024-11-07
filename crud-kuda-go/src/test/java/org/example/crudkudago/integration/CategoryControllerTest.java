package org.example.crudkudago.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.crudkudago.entity.Category;
import org.example.crudkudago.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final static UUID CATEGORY_ID = UUID.fromString("f95c61f8-1faa-4d51-a59e-13975f15e9ca");

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("wiremock/categories.json");

    @DynamicPropertySource
    static void overrideKudaGoProperties(DynamicPropertyRegistry registry) {
        String wireMockUrl = String.format("http://%s:%d",
                wiremockServer.getHost(),
                wiremockServer.getMappedPort(8080));
        registry.add("kudago.category-url", () -> wireMockUrl + "/public-api/v1.4/place-categories");
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        var category = new Category();
        category.setName("Test Category");
        category.setId(CATEGORY_ID);

        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(category));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Category[] categories = objectMapper.readValue(jsonResponse, Category[].class);
        assertThat(categories).hasSize(1);
        assertThat(categories[0].getName()).isEqualTo("Test Category");

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void shouldReturnCategoryById() throws Exception {
        var category = new Category();
        category.setName("Test Category");
        category.setId(CATEGORY_ID);

        when(categoryService.getCategoryById(CATEGORY_ID)).thenReturn(category);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/places/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Category responseCategory = objectMapper.readValue(jsonResponse, Category.class);
        assertThat(responseCategory.getName()).isEqualTo("Test Category");
        assertThat(responseCategory.getId()).isEqualTo(CATEGORY_ID);

        verify(categoryService, times(1)).getCategoryById(CATEGORY_ID);
    }

    @Test
    @DisplayName("POST /api/v1/places/categories - Should create new category")
    void shouldCreateCategory() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("New Category");
        String categoryJson = objectMapper.writeValueAsString(newCategory);

        doNothing().when(categoryService).createCategory(any(Category.class));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");
        String categoryJson = objectMapper.writeValueAsString(updatedCategory);

        doNothing().when(categoryService).updateCategory(any(UUID.class), any(Category.class));

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/places/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).isEmpty();

        verify(categoryService, times(1)).updateCategory(any(UUID.class), any(Category.class));
    }
}


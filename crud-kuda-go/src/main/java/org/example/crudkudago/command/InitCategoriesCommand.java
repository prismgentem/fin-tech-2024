package org.example.crudkudago.command;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.entity.Category;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.service.CategoryService;
import org.example.logexecutionstarter.annotation.LogExecutionTime;
import org.springframework.core.convert.ConversionService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class InitCategoriesCommand implements Command {

    private final CategoryService categoryService;
    private final KudagoClient kudagoClient;
    private final ConversionService conversionService;

    @LogExecutionTime
    @Override
    public void execute() {
        log.info("Initializing categories...");
        List<KudagoCategoryResponse> categories = kudagoClient.fetchCategories();
        categories.forEach(categoryResponse -> {
            var category = conversionService.convert(categoryResponse, Category.class);
            categoryService.createCategory(category);
        });
        log.info("Categories initialized. Total: {}", categories.size());
    }
}

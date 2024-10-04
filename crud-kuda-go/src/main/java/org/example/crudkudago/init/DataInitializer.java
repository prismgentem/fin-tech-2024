package org.example.crudkudago.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.logexecutionstarter.annotation.LogExecutionTime;
import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.entity.Category;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.example.crudkudago.service.CategoryService;
import org.example.crudkudago.service.LocationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final KudagoClient kudagoClient;
    private final ConversionService conversionService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Starting data initialization after application ready...");
        initCategories();
        initLocations();
        log.info("Completed data initialization.");
    }

    @LogExecutionTime
    private void initCategories() {
        log.info("Initializing categories...");
        List<KudagoCategoryResponse> categories = kudagoClient.fetchCategories();
        categories.forEach(categoryResponse -> {
            var category = conversionService.convert(categoryResponse, Category.class);
            categoryService.createCategory(category);
        });
        log.info("Categories initialized. Total: {}", categories.size());
    }

    @LogExecutionTime
    private void initLocations() {
        log.info("Initializing locations...");
        List<KudagoLocationResponse> locations = kudagoClient.fetchLocations();
        locations.forEach(locationResponse -> {
            var location = conversionService.convert(locationResponse, Location.class);
            locationService.createLocation(location);
        });
        log.info("Locations initialized. Total: {}", locations.size());
    }
}

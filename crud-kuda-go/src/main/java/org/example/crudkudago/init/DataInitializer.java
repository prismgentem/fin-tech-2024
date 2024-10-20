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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final KudagoClient kudagoClient;
    private final ConversionService conversionService;
    private final ExecutorService fixedThreadPool;
    private final ScheduledExecutorService scheduledThreadPool;

    @Value("${app.data-init.interval}")
    private long initInterval;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Scheduling data initialization after application ready...");
        scheduledThreadPool.scheduleAtFixedRate(this::initData, 0, initInterval, TimeUnit.SECONDS);
    }

    @LogExecutionTime
    private void initData() {
        log.info("Starting data initialization...");

        try {
            Future<?> categoryFuture = fixedThreadPool.submit(this::initCategories);
            Future<?> locationFuture = fixedThreadPool.submit(this::initLocations);

            categoryFuture.get();
            locationFuture.get();

            log.info("Completed data initialization.");
        } catch (Exception e) {
            log.error("Error during data initialization", e);
            throw new RuntimeException(e);
        }
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

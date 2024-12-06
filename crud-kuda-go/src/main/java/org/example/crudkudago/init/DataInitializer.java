package org.example.crudkudago.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.entity.Category;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.service.CategoryService;
import org.example.crudkudago.service.LocationService;
import org.example.logexecutionstarter.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
    private final ScheduledExecutorService scheduledThreadPool;

    @Value("${app.data-init.interval}")
    private Duration initInterval;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Scheduling data initialization after application ready...");
        long initialDelay = 0L;
        long intervalInSeconds = initInterval.toSeconds();
        scheduledThreadPool.scheduleAtFixedRate(this::initData, initialDelay, intervalInSeconds, TimeUnit.SECONDS);
    }

    @LogExecutionTime
    public void initData() {
        log.info("Starting data initialization...");

        Mono<Void> initCategoriesMono = initCategories();
        Mono<Void> initLocationsMono = initLocations();

        Mono.zip(initCategoriesMono, initLocationsMono)
                .doOnSuccess(tuple -> log.info("Completed data initialization."))
                .doOnError(e -> log.error("Error during data initialization", e))
                .block();
    }

    @LogExecutionTime
    public Mono<Void> initCategories() {
        return kudagoClient.fetchCategories()
                .flatMapMany(Flux::fromIterable)
                .mapNotNull(categoryResponse -> conversionService.convert(categoryResponse, Category.class))
                .flatMap(categoryService::createCategory)
                .then(Mono.fromRunnable(() -> log.info("Categories initialized.")));
    }

    @LogExecutionTime
    public Mono<Void> initLocations() {
        return kudagoClient.fetchLocations()
                .flatMapMany(Flux::fromIterable)
                .mapNotNull(locationResponse -> conversionService.convert(locationResponse, Location.class))
                .flatMap(locationService::createLocation)
                .then(Mono.fromRunnable(() -> log.info("Locations initialized.")));
    }
}

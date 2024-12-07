package org.example.crudkudago;

import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.entity.Category;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.init.DataInitializer;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.example.crudkudago.service.CategoryService;
import org.example.crudkudago.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

class DataInitializerTest {


    @Mock
    private KudagoClient kudagoClient;

    @Mock
    private CategoryService categoryService;
    @Mock
    private LocationService locationService;
    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitCategories_Error() {
        var categoryResponse = new KudagoCategoryResponse();
        categoryResponse.setId(1);
        categoryResponse.setName("name");
        categoryResponse.setSlug("slug");

        when(kudagoClient.fetchCategories()).thenReturn(Mono.error(new RuntimeException("Error fetching categories")));

        StepVerifier.create(dataInitializer.initCategories())
                .expectErrorMessage("Error fetching categories")
                .verify();
    }

    @Test
    void testInitLocations_Error() {
        var locationResponse = new KudagoLocationResponse();
        locationResponse.setName("name");
        locationResponse.setSlug("slug");

        when(kudagoClient.fetchLocations()).thenReturn(Mono.error(new RuntimeException("Error fetching locations")));

        StepVerifier.create(dataInitializer.initLocations())
                .expectErrorMessage("Error fetching locations")
                .verify();
    }


    @Test
    void testInitAllData_Success() {
        var categoryResponse = new KudagoCategoryResponse();
        categoryResponse.setId(1);
        categoryResponse.setName("name");
        categoryResponse.setSlug("slug");

        var locationResponse = new KudagoLocationResponse();
        locationResponse.setName("name");
        locationResponse.setSlug("slug");

        var category = Category.builder()
                .id(UUID.randomUUID())
                .name(categoryResponse.getName())
                .slug(categoryResponse.getSlug())
                .build();

        var location = Location.builder()
                .id(UUID.randomUUID())
                .slug(locationResponse.getSlug())
                .name(locationResponse.getName())
                .build();

        when(kudagoClient.fetchCategories()).thenReturn(Mono.just(List.of(categoryResponse)));
        when(kudagoClient.fetchLocations()).thenReturn(Mono.just(List.of(locationResponse)));

        when(categoryService.createCategory(category)).thenReturn(Mono.empty());
        when(locationService.createLocation(location)).thenReturn(Mono.empty());

        when(dataInitializer.initCategories()).thenReturn(Mono.empty());
        when(dataInitializer.initLocations()).thenReturn(Mono.empty());

        StepVerifier.create(dataInitializer.initCategories())
                .verifyComplete();

        StepVerifier.create(dataInitializer.initLocations())
                .verifyComplete();

        StepVerifier.create(categoryService.createCategory(category))
                .verifyComplete();

        StepVerifier.create(locationService.createLocation(location))
                .verifyComplete();
    }
}

package org.example.crudkudago.unit;

import org.example.crudkudago.entity.Location;
import org.example.crudkudago.exception.LocationNotFoundException;
import org.example.crudkudago.exception.LocationOperationException;
import org.example.crudkudago.repository.InMemoryRepository;
import org.example.crudkudago.service.LocationServiceImpl;
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

class LocationServiceImplTest {

    @Mock
    private InMemoryRepository<Location> locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocationsSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        Map<UUID, Location> locationMap = new HashMap<>();
        locationMap.put(location.getId(), location);

        when(locationRepository.findAll()).thenReturn(locationMap);

        List<Location> locations = locationService.getAllLocations();

        assertNotNull(locations);
        assertEquals(1, locations.size());
        assertEquals(location.getId(), locations.get(0).getId());
    }

    @Test
    void getLocationByIdSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(location);

        Location foundLocation = locationService.getLocationById(location.getId());

        assertNotNull(foundLocation);
        assertEquals(location.getId(), foundLocation.getId());
    }

    @Test
    void getLocationByIdNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(locationRepository.findById(invalidId)).thenReturn(null);

        assertThrows(LocationNotFoundException.class, () -> {
            locationService.getLocationById(invalidId);
        });
    }

    @Test
    void createLocationSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        doNothing().when(locationRepository).save(any(UUID.class), any(Location.class));

        assertDoesNotThrow(() -> locationService.createLocation(location));
        verify(locationRepository, times(1)).save(any(UUID.class), eq(location));
    }

    @Test
    void createLocationException() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        doThrow(new RuntimeException("Mock exception")).when(locationRepository).save(any(UUID.class), any(Location.class));

        assertThrows(LocationOperationException.class, () -> {
            locationService.createLocation(location);
        });
    }

    @Test
    void updateLocationSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(location);
        doNothing().when(locationRepository).update(location.getId(), location);

        assertDoesNotThrow(() -> locationService.updateLocation(location.getId(), location));
        verify(locationRepository, times(1)).update(location.getId(), location);
    }

    @Test
    void updateLocationNotFound() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");
        UUID invalidId = UUID.randomUUID();

        when(locationRepository.findById(invalidId)).thenReturn(null);

        assertThrows(LocationNotFoundException.class, () -> {
            locationService.updateLocation(invalidId, location);
        });
    }

    @Test
    void updateLocationException() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(location);
        doThrow(new RuntimeException("Mock exception")).when(locationRepository).update(location.getId(), location);

        assertThrows(LocationOperationException.class, () -> {
            locationService.updateLocation(location.getId(), location);
        });
    }

    @Test
    void deleteLocationSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(location);
        doNothing().when(locationRepository).delete(location.getId());

        assertDoesNotThrow(() -> locationService.deleteLocation(location.getId()));
        verify(locationRepository, times(1)).delete(location.getId());
    }

    @Test
    void deleteLocationNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(locationRepository.findById(invalidId)).thenReturn(null);

        assertThrows(LocationNotFoundException.class, () -> {
            locationService.deleteLocation(invalidId);
        });
    }

    @Test
    void deleteLocationException() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(location);
        doThrow(new RuntimeException("Mock exception")).when(locationRepository).delete(location.getId());

        assertThrows(LocationOperationException.class, () -> {
            locationService.deleteLocation(location.getId());
        });
    }
}

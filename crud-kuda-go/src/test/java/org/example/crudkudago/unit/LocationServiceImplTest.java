package org.example.crudkudago.unit;

import org.example.crudkudago.entity.Location;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.repository.LocationRepository;
import org.example.crudkudago.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocationsSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        Map<UUID, Location> locationMap = new HashMap<>();
        locationMap.put(location.getId(), location);

        when(locationRepository.findAll()).thenReturn(List.copyOf(locationMap.values()));

        List<Location> locations = locationService.getAllLocations();

        assertNotNull(locations);
        assertEquals(1, locations.size());
        assertEquals(location.getId(), locations.get(0).getId());
    }

    @Test
    void getLocationByIdSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        Location foundLocation = locationService.getLocationById(location.getId());

        assertNotNull(foundLocation);
        assertEquals(location.getId(), foundLocation.getId());
    }

    @Test
    void getLocationByIdNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(locationRepository.findById(invalidId)).thenReturn(Optional.empty());

        var ex = assertThrows(ServiceException.class, () -> {
            locationService.getLocationById(invalidId);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void createLocationSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        doNothing().when(locationRepository).save(eq(location));

        assertDoesNotThrow(() -> locationService.createLocation(location));
        verify(locationRepository, times(1)).save(eq(location));
    }

    @Test
    void createLocationException() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        doThrow(new ServiceException(ErrorType.NOT_FOUND, String.format("Location with id '%s' not found", location.getId()))).when(locationRepository).save(eq(location));

        var ex = assertThrows(ServiceException.class, () -> {
            locationService.createLocation(location);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void updateLocationSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        doNothing().when(locationRepository).save(eq(location));

        assertDoesNotThrow(() -> locationService.updateLocation(location.getId(), location));
        verify(locationRepository, times(1)).save(eq(location));
    }

    @Test
    void updateLocationNotFound() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");
        UUID invalidId = UUID.randomUUID();

        when(locationRepository.findById(invalidId)).thenReturn(Optional.empty());

        var ex = assertThrows(ServiceException.class, () -> {
            locationService.updateLocation(invalidId, location);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void updateLocationException() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        doThrow(new ServiceException(ErrorType.NOT_FOUND, String.format("Location with id '%s' not found", location.getId()))).when(locationRepository).save(eq(location));

        var ex = assertThrows(ServiceException.class, () -> {
            locationService.updateLocation(location.getId(), location);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void deleteLocationSuccess() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        doNothing().when(locationRepository).deleteById(location.getId());

        assertDoesNotThrow(() -> locationService.deleteLocationById(location.getId()));
        verify(locationRepository, times(1)).deleteById(location.getId());
    }

    @Test
    void deleteLocationNotFound() {
        UUID invalidId = UUID.randomUUID();

        when(locationRepository.findById(invalidId)).thenReturn(Optional.empty());

        var ex = assertThrows(ServiceException.class, () -> {
            locationService.deleteLocationById(invalidId);
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }

    @Test
    void deleteLocationException() {
        var location = new Location(UUID.randomUUID(), "slug-test", "Test Category");

        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        doThrow(new ServiceException(ErrorType.NOT_FOUND, String.format("Location with id '%s' not found", location.getId()))).when(locationRepository).deleteById(location.getId());

        var ex = assertThrows(ServiceException.class, () -> {
            locationService.deleteLocationById(location.getId());
        });

        assertEquals(ErrorType.NOT_FOUND.getStatus(), ex.getStatus());
    }
}

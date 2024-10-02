package org.example.crudkudago.controller;

import lombok.RequiredArgsConstructor;
import org.example.crudkudago.annotation.LogExecutionTime;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.service.LocationServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@LogExecutionTime
@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationControllerImpl implements LocationController {
    private final LocationServiceImpl locationService;
    @Override
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @Override
    public Location getLocationById(UUID id) {
        return locationService.getLocationById(id);
    }

    @Override
    public void createLocation(Location location) {
        locationService.createLocation(location);
    }

    @Override
    public void updateLocation(UUID id, Location location) {
        locationService.updateLocation(id, location);
    }

    @Override
    public void deleteLocation(UUID id) {
        locationService.deleteLocation(id);
    }
}

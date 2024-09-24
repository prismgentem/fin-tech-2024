package org.example.crudkudago.controller;

import org.example.crudkudago.entity.Location;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface LocationController {
    @GetMapping
    List<Location> getAllLocations();

    @GetMapping("/{id}")
    Location getLocationById(@PathVariable UUID id);

    @PostMapping
    void createLocation(@RequestBody Location location);

    @PutMapping("/{id}")
    void updateLocation(@PathVariable UUID id, @RequestBody Location location);

    @DeleteMapping("/{id}")
    void deleteLocation(@PathVariable UUID id);
}

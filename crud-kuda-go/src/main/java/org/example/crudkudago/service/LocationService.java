package org.example.crudkudago.service;

import org.example.crudkudago.entity.Location;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    List<Location> getAllLocations();

    Location getLocationById(UUID id);

    void createLocation(Location location);

    void updateLocation(UUID id, Location location);

    void deleteLocation(UUID id);
}

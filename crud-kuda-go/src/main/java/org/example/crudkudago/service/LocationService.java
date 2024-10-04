package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private static final String MSG_LOCATION_NOT_FOUND = "Category with id '%s' not found";
    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_LOCATION_NOT_FOUND, id));
    }

    public void createLocation(Location location) {
        locationRepository.save(location);
    }

    public void updateLocation(UUID id, Location location) {
        locationRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_LOCATION_NOT_FOUND, id));
        locationRepository.save(location);

    }

    public void deleteLocationById(UUID id) {
        locationRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_LOCATION_NOT_FOUND, id));
        locationRepository.deleteById(id);
    }
}

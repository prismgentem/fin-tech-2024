//package org.example.crudkudago.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.crudkudago.entity.Location;
////import org.example.crudkudago.exception.LocationNotFoundException;
////import org.example.crudkudago.exception.LocationOperationException;
//import org.example.crudkudago.repository.InMemoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class LocationServiceImpl implements LocationService {
//    private final InMemoryRepository<Location> locationRepository;
//    @Autowired
//    public LocationServiceImpl(InMemoryRepository<Location> locationRepository) {
//        this.locationRepository = locationRepository;
//    }
//
//    @Override
//    public List<Location> getAllLocations() {
//        return List.copyOf(locationRepository.findAll().values());
//    }
//
//    @Override
//    public Location getLocationById(UUID id) {
//        Location location = locationRepository.findById(id);
//        if (location == null) {
//            log.warn("Location with ID {} not found", id);
////            throw new LocationNotFoundException("Location with ID " + id + " not found");
//        }
//        return location;
//    }
//
//    @Override
//    public void createLocation(Location location) {
//        try {
//            var id = UUID.randomUUID();
//            location.setId(id);
//            locationRepository.save(id, location);
//        } catch (Exception e) {
//            log.error("Error while creating location", e);
////            throw new LocationOperationException("Failed to create location", e);
//        }
//    }
//
//    @Override
//    public void updateLocation(UUID id, Location location) {
//        if (locationRepository.findById(id) == null) {
//            log.warn("Location with ID {} not found for update", id);
////            throw new LocationNotFoundException("Location with ID " + id + " not found");
//        }
//        try {
//            locationRepository.update(id, location);
//        } catch (Exception e) {
//            log.error("Error while updating location with ID {}", id, e);
////            throw new LocationOperationException("Failed to update location", e);
//        }
//    }
//
//    @Override
//    public void deleteLocationById(UUID id) {
//        if (locationRepository.findById(id) == null) {
//            log.warn("Location with ID {} not found for deletion", id);
////            throw new LocationNotFoundException("Location with ID " + id + " not found");
//        }
//        try {
//            locationRepository.delete(id);
//        } catch (Exception e) {
//            log.error("Error while deleting location with ID {}", id, e);
////            throw new LocationOperationException("Failed to delete location", e);
//        }
//    }
//}

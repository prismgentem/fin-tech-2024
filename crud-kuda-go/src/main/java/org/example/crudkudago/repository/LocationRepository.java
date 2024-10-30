package org.example.crudkudago.repository;

import org.example.crudkudago.entity.Location;
import org.example.crudkudago.observer.Observer;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class LocationRepository implements EntityRepository<UUID, Location> {

    private final Map<UUID, Location> locations = new ConcurrentHashMap<>();
    private final List<Observer<Location>> observers = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Location> findById(UUID id) {
        return locations.containsKey(id)
                ? Optional.of(locations.get(id))
                : Optional.empty();
    }

    @Override
    public List<Location> findAll() {
        return locations.values().stream().toList();
    }

    @Override
    public void save(Location entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(UUID.randomUUID());
        }
        locations.put(entity.getId(), entity);
    }

    @Override
    public void deleteById(UUID id) {
        locations.remove(id);
    }

    @Override
    public void addObserver(Observer<Location> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Location> observer) {
        observers.remove(observer);
    }

    private void notifyObserversOnSave(Location entity) {
        for (Observer<Location> observer : observers) {
            observer.onSave(entity);
        }
    }

    private void notifyObserversOnDelete(Location entity) {
        for (Observer<Location> observer : observers) {
            observer.onDelete(entity);
        }
    }
}

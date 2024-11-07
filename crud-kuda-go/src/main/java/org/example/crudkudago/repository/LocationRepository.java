package org.example.crudkudago.repository;

import org.example.crudkudago.entity.Location;
import org.example.crudkudago.model.snapshot.LocationSnapshot;
import org.example.crudkudago.observer.Observer;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class LocationRepository implements EntityRepository<UUID, Location> {

    private final Map<UUID, Location> locations = new ConcurrentHashMap<>();
    private final List<Observer<Location>> observers = new CopyOnWriteArrayList<>();
    private final Map<UUID, List<LocationSnapshot>> history = new ConcurrentHashMap<>();


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
        } else {
            saveSnapshot(entity);
        }
        locations.put(entity.getId(), entity);
        notifyObserversOnSave(entity);
    }

    @Override
    public void deleteById(UUID id) {
        var removed = locations.remove(id);
        if (removed != null) {
            notifyObserversOnDelete(removed);
        }
    }

    @Override
    public void addObserver(Observer<Location> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Location> observer) {
        observers.remove(observer);
    }

    private void saveSnapshot(Location entity) {
        List<LocationSnapshot> snapshots = history.computeIfAbsent(entity.getId(), k -> new ArrayList<>());
        snapshots.add(new LocationSnapshot(entity.getId(), entity.getSlug(), entity.getName()));
    }

    public List<LocationSnapshot> getHistory(UUID id) {
        return history.getOrDefault(id, Collections.emptyList());
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

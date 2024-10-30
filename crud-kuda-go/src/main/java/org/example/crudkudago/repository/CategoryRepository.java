package org.example.crudkudago.repository;

import org.example.crudkudago.entity.Category;
import org.example.crudkudago.model.snapshot.CategorySnapshot;
import org.example.crudkudago.observer.Observer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class CategoryRepository implements EntityRepository<UUID, Category>{

    private final Map<UUID, Category> categories = new ConcurrentHashMap<>();
    private final List<Observer<Category>> observers = new CopyOnWriteArrayList<>();
    private final Map<UUID, List<CategorySnapshot>> history = new ConcurrentHashMap<>();


    @Override
    public Optional<Category> findById(UUID id) {
        return categories.containsKey(id)
                ? Optional.of(categories.get(id))
                : Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return categories.values().stream().toList();
    }

    @Override
    public void save(Category entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(UUID.randomUUID());
        } else {
            saveSnapshot(entity);
        }
        categories.put(entity.getId(), entity);
    }

    @Override
    public void deleteById(UUID id) {
        categories.remove(id);
    }

    @Override
    public void addObserver(Observer<Category> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Category> observer) {
        observers.remove(observer);
    }

    private void saveSnapshot(Category entity) {
        List<CategorySnapshot> snapshots = history.computeIfAbsent(entity.getId(), k -> new ArrayList<>());
        snapshots.add(new CategorySnapshot(entity.getId(), entity.getSlug(), entity.getName()));
    }

    public List<CategorySnapshot> getHistory(UUID id) {
        return history.getOrDefault(id, Collections.emptyList());
    }

    private void notifyObserversOnSave(Category entity) {
        for (Observer<Category> observer : observers) {
            observer.onSave(entity);
        }
    }

    private void notifyObserversOnDelete(Category entity) {
        for (Observer<Category> observer : observers) {
            observer.onDelete(entity);
        }
    }
}
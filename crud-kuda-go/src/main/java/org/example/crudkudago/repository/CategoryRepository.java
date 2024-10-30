package org.example.crudkudago.repository;

import org.example.crudkudago.entity.Category;
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
public class CategoryRepository implements EntityRepository<UUID, Category>{

    private final Map<UUID, Category> categories = new ConcurrentHashMap<>();
    private final List<Observer<Category>> observers = new CopyOnWriteArrayList<>();

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
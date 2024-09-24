package org.example.crudkudago.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T> {
    private final Map<UUID, T> repository = new ConcurrentHashMap<>();

    public void save(UUID id,T entity) {
        repository.put(id, entity);
    }

    public T findById(UUID id) {
        return repository.get(id);
    }

    public Map<UUID, T> findAll() {
        return repository;
    }

    public void update(UUID id, T entity) {
        repository.put(id, entity);
    }

    public void delete(UUID id) {
        repository.remove(id);
    }
}

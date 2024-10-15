package org.example.crudkudago.repository;

import org.example.crudkudago.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CategoryRepository implements EntityRepository<UUID, Category>{

    private final Map<UUID, Category> categories = new ConcurrentHashMap<>();

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

}
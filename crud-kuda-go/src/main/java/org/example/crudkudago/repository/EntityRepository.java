package org.example.crudkudago.repository;

import org.example.crudkudago.observer.Observer;

import java.util.List;
import java.util.Optional;

public interface EntityRepository <I, E>{
    Optional<E> findById(I id);
    List<E> findAll();
    void save(E entity);
    void deleteById(I id);
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
}

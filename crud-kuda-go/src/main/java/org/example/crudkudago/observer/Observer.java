package org.example.crudkudago.observer;

public interface Observer<E> {
    void onSave(E entity);
    void onDelete(E entity);
}

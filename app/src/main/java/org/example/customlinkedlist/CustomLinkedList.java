package org.example.customlinkedlist;

import lombok.*;

import java.util.AbstractList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public class CustomLinkedList<T> extends AbstractList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Node<T> {
        private T value;
        private Node<T> next;
        private Node<T> prev;
    }

    @Override
    public boolean add(T element) {
        var newNode = new Node<T>(element, null, tail);
        if (tail != null) {
            tail.setNext(newNode);
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
        size++;
        return true;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).getValue();
    }

    private Node<T> getNode(int index) {
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> current = getNode(index);

        if (current == head) {
            head = current.getNext();
            if (head != null) {
                head.setPrev(null);
            } else {
                tail = null;
            }
        } else if (current == tail) {
            tail = current.getPrev();
            if (tail != null) {
                tail.setNext(null);
            } else {
                head = null;
            }
        } else {
            Node<T> prevNode = current.getPrev();
            Node<T> nextNode = current.getNext();

            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }

        size--;

        return current.getValue();
    }

    @Override
    public boolean contains(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (current.getValue().equals(o)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T element : c) {
            add(element);
        }
        return !c.isEmpty();
    }

    public int size() {
        return size;
    }

}

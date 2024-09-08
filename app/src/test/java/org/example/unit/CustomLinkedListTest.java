package org.example.unit;

import org.example.customlinkedlist.CustomLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testAddAndSize() {
        list.add(1);
        list.add(2);
        assertEquals(2, list.size());
    }

    @Test
    void testGet() {
        list.add(1);
        list.add(2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    void testRemove() {
        list.add(1);
        list.add(2);
        list.remove(0);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    void testContains() {
        list.add(1);
        list.add(2);
        assertTrue(list.contains(1));
        assertFalse(list.contains(3));
    }

    @Test
    void testAddAll() {
        list.addAll(List.of(1, 2, 3));
        assertEquals(3, list.size());
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertTrue(list.contains(3));
    }

    @Test
    void testAddAllEmptyCollection() {
        list.addAll(List.of());
        assertEquals(0, list.size());
    }

    @Test
    void testRemoveOutOfBounds() {
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void testGetOutOfBounds() {
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testAddAndRemove() {
        list.add(1);
        list.add(2);
        list.remove(1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
}


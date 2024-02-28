package com.grupo01.DataStructuresProject.datastructures.hashmap;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grupo01.DataStructuresProject.datastructures.linkedlist.LinkedList;
import com.grupo01.DataStructuresProject.service.HashMapSerializer;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

@ToString
@Getter
@JsonSerialize(using = HashMapSerializer.class)
public class HashMap<Key, Value> {
    private LinkedList<Entry<Key, Value>>[] table;
    private int capacity = 11;
    private int size = 0;
    private double loadFactor = 0.75;

    public HashMap() {
        table = new LinkedList[capacity];
        for(int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public HashMap(int capacity) {
        this.capacity = capacity;
        table = new LinkedList[capacity];
        for(int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public HashMap(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        table = new LinkedList[capacity];
        for(int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private void checkSize() {
        if (size >= capacity * loadFactor) {
            int newCapacity = capacity * 2;
            var newTable = new LinkedList[capacity];
            for (var list : table) {
                if (list != null) {
                    list.forEach(entry -> {
                        int index = entry.getKey().hashCode() % newCapacity;
                        if (index < 0) {
                            index += newCapacity;
                        }
                        if (newTable[index] == null) {
                            newTable[index] = new LinkedList<Entry<Key, Value>>();
                        }
                        newTable[index].add(entry);
                    });
                }
            }
            table = newTable;
            capacity = newCapacity;
        }
    }

    public void put(Key key, Value value) {
        checkSize();
        if (containsKey(key)) {
            remove(key);
        }
        int index = key.hashCode() % capacity;
        if (index < 0) {
            index += capacity;
        }
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        table[index].add(new Entry<>(key, value));
        size++;
    }

    public Value get(Key key) {
        int index = key.hashCode() % capacity;
        if (index < 0) {
            index += capacity;
        }
        if (table[index] == null) {
            return null;
        }
        AtomicReference<Value> value = new AtomicReference<>();
        table[index].forEach(entry -> {
            if (entry.getKey().equals(key)) {
                value.set(entry.getValue());
            }
        });
        return value.get();
    }

    public void remove(Key key) {
        int index = key.hashCode() % capacity;
        if (index < 0) {
            index += capacity;
        }
        if (table[index] == null) {
            return;
        }
        int finalIndex = index;
        table[index].forEach(entry -> {
            if (entry.getKey().equals(key)) {
                table[finalIndex].remove(entry);
            }
        });
        size--;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Key key) {
        int index = key.hashCode() % capacity;
        if (index < 0) {
            index += capacity;
        }
        if (table[index] == null) {
            return false;
        }
        AtomicReference<Boolean> contains = new AtomicReference<>(false);
        table[index].forEach(entry -> {
            if (entry.getKey().equals(key)) {
                contains.set(true);
            }
        });
        return contains.get();
    }

    public boolean containsValue(Value value) {
        AtomicReference<Boolean> contains = new AtomicReference<>(false);
        for (var list : table) {
            if (list != null) {
                list.forEach(entry -> {
                    if (entry.getValue().equals(value)) {
                        contains.set(true);
                    }
                });
            }
        }
        return contains.get();
    }

    public void clear() {
        table = new LinkedList[capacity];
        size = 0;
    }

    public LinkedList<Key> keySet() {
        LinkedList<Key> keys = new LinkedList<>();
        for (var list : table) {
            if (list != null) {
                list.forEach(entry -> keys.add(entry.getKey()));
            }
        }
        return keys;
    }

    public void forEach(BiConsumer<? super Key, ? super Value> action) {
        for (var list : table) {
            if (list != null) {
                list.forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
            }
        }
    }

}

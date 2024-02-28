package com.grupo01.DataStructuresProject.datastructures.linkedlist;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.function.Consumer;

@NoArgsConstructor
@ToString
public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void add(T data) {
        add(data, size);
    }

    public void add(T data, int index) {
        checkIndex(index, true);
        if (size == 0) {
            addFirst(data);
        } else if (index == size) {
            addLast(data);
        } else {
            Node<T> temp = head;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.getNext();
            }
            Node<T> newNode = new Node<>(data);
            newNode.setNext(temp.getNext());
            temp.setNext(newNode);
            size++;
        }
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(head);
        head = newNode;
        if (size == 0) {
            tail = head;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        tail.setNext(newNode);
        tail = newNode;
        size++;
    }

    public T remove(T data) {
        return remove(indexOf(data));
    }

    public T remove(int index) {
        checkIndex(index, false);
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        }
        Node<T> temp = head;
        for (int i = 0; i < index - 1; i++) {
            temp = temp.getNext();
        }
        T data = temp.getNext().getData();
        temp.setNext(temp.getNext().getNext());
        size--;
        return data;
    }

    public T removeFirst() {
        if (head == null) {
            return null;
        }
        T data = head.getData();
        head = head.getNext();
        size--;
        if (size == 0) {
            tail = null;
        }
        return data;
    }

    public T removeLast() {
        if (size <= 1) {
            return removeFirst();
        }
        Node<T> temp = head;
        for (int i = 0; i < size - 2; i++) {
            temp = temp.getNext();
        }
        T data = tail.getData();
        tail = temp;
        tail.setNext(null);
        size--;
        return data;
    }

    public int indexOf(T data) {
        Node<T> temp = head;
        for (int i = 0; i < size; i++) {
            if (temp.getData().equals(data)) {
                return i;
            }
            temp = temp.getNext();
        }
        return -1;
    }

    public int lastIndexOf(T data) {
        Node<T> temp = head;
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (temp.getData().equals(data)) {
                index = i;
            }
            temp = temp.getNext();
        }
        return index;
    }

    public T get(int index) {
        checkIndex(index, false);
        Node<T> temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        return temp.getData();
    }

    public T getFirst() {
        return head.getData();
    }

    public T getLast() {
        return tail.getData();
    }

    public T set(T data, int index) {
        checkIndex(index, false);
        Node<T> temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        temp.setData(data);
        return data;
    }

    public T setFirst(T data) {
        head.setData(data);
        return head.getData();
    }

    public T setLast(T data) {
        tail.setData(data);
        return tail.getData();
    }

    public boolean contains(T data) {
        return indexOf(data) != -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private void checkIndex(int index, boolean isAddOperation) {
        int max = isAddOperation ? size : size - 1;
        if (index < 0 || index > max) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public void forEach(Consumer<T> consumer) {
        Node<T> temp = head;
        while (temp != null) {
            consumer.accept(temp.getData());
            temp = temp.getNext();
        }
    }
}

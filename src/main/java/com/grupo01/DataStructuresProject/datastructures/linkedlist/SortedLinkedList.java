package com.grupo01.DataStructuresProject.datastructures.linkedlist;

import com.grupo01.DataStructuresProject.datastructures.Comparable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SortedLinkedList<T extends Comparable<T>> {
    private LLNode<T> head = null;
    private int size = 0;

    public void insert(T data) {
        LLNode<T> newNode = new LLNode<>(data);
        if (head == null || head.getData().lowerThan(data)) {
            newNode.setNext(head);
            head = newNode;
        } else {
            LLNode<T> current = head;
            while (current.getNext() != null && current.getNext().getData().lowerOrEqualTo(data)) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
        size++;
    }

    public void delete(T data) {
        if (head == null) {
            return;
        }
        if (head.getData().equalTo(data)) {
            head = head.getNext();
            return;
        }
        LLNode<T> current = head;
        while (current.getNext() != null && !current.getNext().getData().equalTo(data)) {
            current = current.getNext();
        }
        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext());
        }
        size--;
    }

    public T search(T data) {
        LLNode<T> current = head;
        while (current != null && !current.getData().equalTo(data)) {
            current = current.getNext();
        }
        return current != null ? current.getData() : null;
    }

    public boolean isEmpty() {
        return head == null;
    }
}

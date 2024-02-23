package com.grupo01.DataStructuresProject.datastructures.queue;

public class Queue<T> {
    private QNode<T> front;
    private QNode<T> rear;

    public Queue() {
        this.front = this.rear = null;
    }

    public void enqueue(T data) {
        QNode<T> newNode = new QNode<>(data);
        if (this.rear == null) {
            this.front = this.rear = newNode;
            return;
        }
        this.rear.setNext(newNode);
        this.rear = newNode;
    }

    public T dequeue() {
        if (this.front == null) {
            return null;
        }
        QNode<T> temp = this.front;
        this.front = this.front.getNext();
        if (this.front == null) {
            this.rear = null;
        }
        return temp.getData();
    }

    public T peek() {
        return this.front != null ? this.front.getData() : null;
    }

    public boolean isEmpty() {
        return this.front == null;
    }
}

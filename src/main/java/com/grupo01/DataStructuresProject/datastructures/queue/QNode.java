package com.grupo01.DataStructuresProject.datastructures.queue;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class QNode<T> {
    private T data;
    private QNode<T> next;

    public QNode(T data) {
        this.data = data;
        this.next = null;
    }
}

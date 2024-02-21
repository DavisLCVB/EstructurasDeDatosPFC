package com.grupo01.DataStructuresProject.datastructures.linkedlist;

import com.grupo01.DataStructuresProject.datastructures.Comparable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LLNode<T>{
    private T data;
    private LLNode<T> next;

    public LLNode(T data) {
        this.data = data;
    }
}

package com.grupo01.DataStructuresProject.datastructures;


public interface Comparable<T>{
    boolean lowerThan(T other);
    boolean greaterThan(T other);
    boolean equalTo(T other);
    boolean lowerOrEqualTo(T other);
    boolean greaterOrEqualTo(T other);
}

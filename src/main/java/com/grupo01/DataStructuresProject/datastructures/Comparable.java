package com.grupo01.DataStructuresProject.datastructures;

import com.grupo01.DataStructuresProject.dao.GenericDAO;

public interface Comparable<T>{
    boolean lowerThan(T other);
    boolean greaterThan(T other);
    boolean equalTo(T other);
    boolean lowerOrEqualTo(T other);
    boolean greaterOrEqualTo(T other);
}

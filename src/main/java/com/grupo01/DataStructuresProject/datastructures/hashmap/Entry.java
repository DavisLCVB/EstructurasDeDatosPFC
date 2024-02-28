package com.grupo01.DataStructuresProject.datastructures.hashmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Entry <key, value>{
    private key key;
    private value value;
}

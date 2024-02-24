package com.grupo01.DataStructuresProject.utils;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ScheduleFunc<T> {
    List<T> monday;
    List<T> tuesday;
    List<T> wednesday;
    List<T> thursday;
    List<T> friday;
    List<T> saturday;
    List<T> sunday;
}

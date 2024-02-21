package com.grupo01.DataStructuresProject.utils;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Schedule {
    private List<TimeLapse> monday;
    private List<TimeLapse> tuesday;
    private List<TimeLapse> wednesday;
    private List<TimeLapse> thursday;
    private List<TimeLapse> friday;
    private List<TimeLapse> saturday;
    private List<TimeLapse> sunday;
}

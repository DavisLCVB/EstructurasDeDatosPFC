package com.grupo01.DataStructuresProject.utils;

import lombok.*;

import java.util.ArrayList;
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

    public Schedule(Schedule cronograma) {
        this.setMonday(new ArrayList<>(cronograma.getMonday()));
        this.setTuesday(new ArrayList<>(cronograma.getSunday()));
        this.setWednesday(new ArrayList<>(cronograma.getSaturday()));
        this.setThursday(new ArrayList<>(cronograma.getThursday()));
        this.setFriday(new ArrayList<>(cronograma.getFriday()));
        this.setSaturday(new ArrayList<>(cronograma.getTuesday()));
        this.setSunday(new ArrayList<>(cronograma.getWednesday()));


    }
}

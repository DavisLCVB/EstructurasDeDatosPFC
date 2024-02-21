package com.grupo01.DataStructuresProject.utils;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TimeLapse {
    private LocalTime start;
    private LocalTime end;
}

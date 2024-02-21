package com.grupo01.DataStructuresProject.utils;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DateTimeLapse {
    private LocalDateTime start;
    private LocalDateTime end;
}

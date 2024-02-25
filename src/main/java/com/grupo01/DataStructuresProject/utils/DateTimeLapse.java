package com.grupo01.DataStructuresProject.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DateTimeLapse implements Lapses {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end;

    @Override
    @JsonIgnore
    public int getDuration() {
        var s = start.getHour() * 60 + start.getMinute();
        var e = end.getHour() * 60 + end.getMinute();
        return e - s;
    }
}

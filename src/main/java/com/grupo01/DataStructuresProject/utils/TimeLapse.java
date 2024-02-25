package com.grupo01.DataStructuresProject.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TimeLapse implements Lapses {
    @JsonFormat(pattern = "HH:mm", timezone = "GMT-5")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT-5")
    private LocalTime end;

    @Override
    @JsonIgnore
    public int getDuration() {
        return this.end.getMinute() - this.start.getMinute();

    }
}

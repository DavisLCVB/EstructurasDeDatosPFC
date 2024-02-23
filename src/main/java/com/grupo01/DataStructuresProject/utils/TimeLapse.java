package com.grupo01.DataStructuresProject.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;


import java.time.Duration;
import java.time.LocalTime;


import static org.springframework.data.mongodb.core.mapping.FieldType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TimeLapse {
    @JsonFormat(pattern = "HH:mm", timezone = "GMT-5")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT-5")
    private LocalTime end;
    public int getDuration(){
        return this.end.getMinute() - this.start.getMinute();

    }


}

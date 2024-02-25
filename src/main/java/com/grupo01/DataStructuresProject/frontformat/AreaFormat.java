package com.grupo01.DataStructuresProject.frontformat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo01.DataStructuresProject.models.Area;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AreaFormat implements JsonMasticadito {
    private String areaID;
    private String areaName;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime areaDuration;
    public AreaFormat(Area area){
        this.areaID = area.getId();
        this.areaName = area.getName();
        this.areaDuration = area.getDuration();
    }
}

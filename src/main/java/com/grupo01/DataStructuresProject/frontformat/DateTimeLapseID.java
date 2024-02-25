package com.grupo01.DataStructuresProject.frontformat;

import com.grupo01.DataStructuresProject.utils.DateTimeLapse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DateTimeLapseID extends DateTimeLapse implements JsonMasticadito{

    public DateTimeLapseID(LocalDateTime start, LocalDateTime end) {
        super(start, end);
        professionalID = new ArrayList<>();
    }

    List<ProfesionalIDFormat> professionalID;
}

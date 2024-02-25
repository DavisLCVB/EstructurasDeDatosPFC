package com.grupo01.DataStructuresProject.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo01.DataStructuresProject.utils.Identify;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Area  implements Identify {
    private String id;
    private String name;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime duration;
}

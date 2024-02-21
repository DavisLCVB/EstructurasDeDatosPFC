package com.grupo01.DataStructuresProject.models;

import com.grupo01.DataStructuresProject.utils.Identify;
import com.grupo01.DataStructuresProject.utils.Schedule;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "professionalUsers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProfessionalUser extends GenericUser implements Identify {
    private String idArea;
    private Schedule availableHours;
}

package com.grupo01.DataStructuresProject.frontformat;

import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProfesionalIDFormat implements JsonMasticadito {
    String id;
    String fullName;

    public ProfesionalIDFormat(ProfessionalUser professionalUser) {
        this.id = professionalUser.getId();
        this.fullName = professionalUser.getFirstName() + " " + professionalUser.getLastName();
    }
}

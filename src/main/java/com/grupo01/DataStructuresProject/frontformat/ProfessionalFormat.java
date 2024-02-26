package com.grupo01.DataStructuresProject.frontformat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import com.grupo01.DataStructuresProject.utils.Schedule;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ProfessionalFormat implements JsonMasticadito{
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime registrationDate;
    private String idArea;
    private Schedule availableHours;

    // Constructor que acepta ProfessionalUser y completa los campos
    public ProfessionalFormat(ProfessionalUser originalProfessional) {
        this.id = originalProfessional.getId();
        this.firstName = originalProfessional.getFirstName();
        this.lastName = originalProfessional.getLastName();
        this.email = originalProfessional.getEmail();
        this.password = originalProfessional.getPassword();
        this.registrationDate = originalProfessional.getRegistrationDate();
        this.idArea = originalProfessional.getIdArea();
        this.availableHours = originalProfessional.getAvailableHours();
    }
}

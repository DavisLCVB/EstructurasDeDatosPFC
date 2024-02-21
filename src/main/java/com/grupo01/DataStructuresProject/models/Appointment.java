package com.grupo01.DataStructuresProject.models;

import com.grupo01.DataStructuresProject.utils.AppointmentStatus;
import com.grupo01.DataStructuresProject.utils.DateTimeLapse;
import com.grupo01.DataStructuresProject.utils.Identify;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Appointment implements Identify {
    @Id
    private String id;
    private String idPatient;
    private String idProfessional;
    private String idArea;
    private DateTimeLapse date;
    private AppointmentStatus status;
}

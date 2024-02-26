package com.grupo01.DataStructuresProject.frontformat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.utils.AppointmentStatus;
import com.grupo01.DataStructuresProject.utils.DateTimeLapse;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentFormat implements JsonMasticadito{
    private String citaId;
    private String pacienteId;
    private String pacienteFullName;
    private String profesionalId;
    private String professionalFullName;
    private String areaId;
    private String areaName;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime areaDuration;
    private DateTimeLapse date;
    private AppointmentStatus Status;


    public AppointmentFormat(Appointment originalAppointment, String patientFirstName, String patientLastName, String profesionalFirstName, String professionalLastName, String areaName, LocalTime areaDuration) {

        this.citaId = originalAppointment.getId();
        this.pacienteId = originalAppointment.getIdPatient();
        this.profesionalId = originalAppointment.getIdProfessional();
        this.areaId = originalAppointment.getIdArea();
        this.date = originalAppointment.getDate();
        this.Status = originalAppointment.getStatus();

        this.pacienteFullName = patientFirstName + " " + patientLastName;
        this.professionalFullName = profesionalFirstName + " " + professionalLastName;
        this.areaName = areaName;
        this.areaDuration = areaDuration;
    }
}

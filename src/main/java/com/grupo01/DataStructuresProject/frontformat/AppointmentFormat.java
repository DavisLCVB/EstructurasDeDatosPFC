package com.grupo01.DataStructuresProject.frontformat;

import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.utils.AppointmentStatus;
import com.grupo01.DataStructuresProject.utils.DateTimeLapse;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentFormat {
    private String citaId;
    private String pacienteId;
    private String pacienteFullName;
    private String profesionalId;
    private String professionalFullName;
    private String areaId;
    private String areaName;
    private LocalTime areaDuration;
    private DateTimeLapse date;
    private AppointmentStatus Status;


    public AppointmentFormat(Appointment originalAppointment, String patientFullName,
                             String professionalFullName, String areaName, LocalTime areaDuration) {

        this.citaId = originalAppointment.getId();
        this.pacienteId = originalAppointment.getIdPatient();
        this.profesionalId =originalAppointment.getIdProfessional();
        this.areaId = originalAppointment.getIdArea();
        this.date = originalAppointment.getDate();
        this.Status = originalAppointment.getStatus();

        this.pacienteFullName = patientFullName;
        this.professionalFullName = professionalFullName;
        this.areaName = areaName;
        this.areaDuration = areaDuration;
    }
}
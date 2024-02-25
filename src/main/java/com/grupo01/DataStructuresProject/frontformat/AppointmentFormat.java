package com.grupo01.DataStructuresProject.frontformat;

import com.grupo01.DataStructuresProject.models.Appointment;
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
public class AppointmentFormat extends Appointment {
    private String patientFullName;
    private String professionalName;
    private String areaName;
    private LocalTime areaDuration;
    private LocalDateTime StartTime;
    private LocalDateTime EndTime;

    public AppointmentFormat(Appointment originalAppointment, String patientFullName,
                             String professionalName, String areaName, LocalTime areaDuration) {

        super(originalAppointment.getId(),
                originalAppointment.getIdPatient(),
                originalAppointment.getIdProfessional(),
                originalAppointment.getIdArea(),
                originalAppointment.getDate(),
                originalAppointment.getStatus());

        this.patientFullName = patientFullName;
        this.professionalName = professionalName;
        this.areaName = areaName;
        this.areaDuration = areaDuration;
        this.StartTime = originalAppointment.getDate().getStart();
        this.EndTime = originalAppointment.getDate().getEnd();

    }
}
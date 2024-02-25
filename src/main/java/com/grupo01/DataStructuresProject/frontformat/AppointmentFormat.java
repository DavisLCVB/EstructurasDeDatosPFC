package com.grupo01.DataStructuresProject.frontformat;

import com.grupo01.DataStructuresProject.models.Appointment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentFormat extends Appointment {
    private String patientName;
    private String professionalName;
    private String areaName;

    public AppointmentFormat(Appointment originalAppointment, String patientName, String professionalName, String areaName) {

        super(originalAppointment.getId(),
                originalAppointment.getIdPatient(),
                originalAppointment.getIdProfessional(),
                originalAppointment.getIdArea(),
                originalAppointment.getDate(),
                originalAppointment.getStatus());

        this.patientName = patientName;
        this.professionalName = professionalName;
        this.areaName = areaName;
    }
}
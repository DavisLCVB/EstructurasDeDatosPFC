package com.grupo01.DataStructuresProject.frontformat;


import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.utils.AppointmentStatus;
import com.grupo01.DataStructuresProject.utils.DateTimeLapse;
import java.time.LocalTime;

public class AppointmentFormat {

    private String citaId;

    private String patientId;

    private String patientFullName;

    private String professionalId;

    private String professionalName;

    private String areaId;

    private String areaName;

    private LocalTime areaDuration;

    private DateTimeLapse date;

    private AppointmentStatus status;

    // Constructor que acepta Appointment y completa los campos
    public AppointmentFormat(Appointment originalAppointment, String patientFullName, String professionalName, String areaName, LocalTime areaDuration) {
        this.citaId = originalAppointment.getId();
        this.patientId = originalAppointment.getIdPatient();
        this.professionalId = originalAppointment.getIdProfessional();
        this.areaId = originalAppointment.getIdArea();
        this.date = originalAppointment.getDate();
        this.status = originalAppointment.getStatus();

        this.patientFullName = patientFullName;
        this.professionalName = professionalName;
        this.areaName = areaName;
        this.areaDuration = areaDuration;
    }


}

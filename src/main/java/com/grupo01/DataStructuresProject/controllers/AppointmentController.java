package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
import com.grupo01.DataStructuresProject.dao.AreaDAOImp;
import com.grupo01.DataStructuresProject.dao.PatientDAOImp;
import com.grupo01.DataStructuresProject.dao.ProfessionalDAOImp;
import com.grupo01.DataStructuresProject.frontformat.AppointmentFormat;
import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentDAOImp appointmentDAOImp;
    @Autowired
    private ProfessionalDAOImp professionalDAOImp;
    @Autowired
    private PatientDAOImp patientDAOImp;
    @Autowired
    private AreaDAOImp areaDAOImp;
    @Autowired
    private IDGenerator idGenerator;

    @GetMapping(value = "/findAll/{idPatient}")
    public Flux<AppointmentFormat> findAll(@PathVariable String idPatient) {
        return appointmentDAOImp.findAllByIdPatient(idPatient).flatMap(e -> {
            String idProfessional = e.getIdProfessional();
            String idArea = e.getIdArea();
            return areaDAOImp.findById(idArea)
                    .flatMap(area -> professionalDAOImp.findById(idProfessional)
                            .flatMap(professional -> patientDAOImp.findById(idPatient)
                                    .map(patient -> new AppointmentFormat(e, patient.getFirstName(), patient.getLastName(), professional.getFirstName(), professional.getLastName(), area.getName(), area.getDuration()))
                            ));
        });
    }

    @GetMapping(value = "/getId")
    public String getID(){
        return idGenerator.generateAppointmentID();
    }

    @GetMapping(value = "/find/{id}")
    public Mono<AppointmentFormat> findById(@PathVariable String id) {
        return appointmentDAOImp.findById(id)
                .flatMap(appointment -> {
                    String idPatient = appointment.getIdPatient();
                    String idProfessional = appointment.getIdProfessional();
                    String idArea = appointment.getIdArea();
                    return areaDAOImp.findById(idArea)
                            .flatMap(area -> professionalDAOImp.findById(idProfessional)
                                    .flatMap(professional -> patientDAOImp.findById(idPatient)
                                            .map(patient -> new AppointmentFormat(appointment, patient.getFirstName(), patient.getLastName(), professional.getFirstName(), professional.getLastName(), area.getName(), area.getDuration()))
                                    ));
                });
    }

    @PostMapping(value = "/save")
    public Mono<AppointmentFormat> save(@RequestBody Appointment appointment) {
        appointment.setId(idGenerator.generateAppointmentID());
        return appointmentDAOImp.save(appointment)
                .flatMap(e -> {
                    String idPatient = e.getIdPatient();
                    String idProfessional = e.getIdProfessional();
                    String idArea = e.getIdArea();
                    return areaDAOImp.findById(idArea)
                            .flatMap(area -> professionalDAOImp.findById(idProfessional)
                                    .flatMap(professional -> patientDAOImp.findById(idPatient)
                                            .map(patient -> new AppointmentFormat(e, patient.getFirstName(), patient.getLastName(), professional.getFirstName(), professional.getLastName(), area.getName(), area.getDuration()))
                                    ));
                });
    }

    @PostMapping(value = "/saveWId")
    public Mono<AppointmentFormat> saveWId(@RequestBody Appointment appointment) {
        return appointmentDAOImp.save(appointment)
                .flatMap(e -> {
                    String idPatient = e.getIdPatient();
                    String idProfessional = e.getIdProfessional();
                    String idArea = e.getIdArea();
                    return areaDAOImp.findById(idArea)
                            .flatMap(area -> professionalDAOImp.findById(idProfessional)
                                    .flatMap(professional -> patientDAOImp.findById(idPatient)
                                            .map(patient -> new AppointmentFormat(e, patient.getFirstName(), patient.getLastName(), professional.getFirstName(), professional.getLastName(), area.getName(), area.getDuration()))
                                    ));
                });
    }

    @PutMapping(value = "/update/{id}")
    public Mono<AppointmentFormat> update(@PathVariable String id, @RequestBody Appointment appointment) {
        return appointmentDAOImp.update(id, appointment)
                .flatMap(e -> {
                    String idPatient = e.getIdPatient();
                    String idProfessional = e.getIdProfessional();
                    String idArea = e.getIdArea();
                    return areaDAOImp.findById(idArea)
                            .flatMap(area -> professionalDAOImp.findById(idProfessional)
                                    .flatMap(professional -> patientDAOImp.findById(idPatient)
                                            .map(patient -> new AppointmentFormat(e, patient.getFirstName(), patient.getLastName(), professional.getFirstName(), professional.getLastName(), area.getName(), area.getDuration()))
                                    ));
                });
    }
}

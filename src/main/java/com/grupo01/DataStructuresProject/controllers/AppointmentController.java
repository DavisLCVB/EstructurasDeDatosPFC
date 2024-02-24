package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
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
    private IDGenerator idGenerator;

    @GetMapping(value = "/findAll")
    public Flux<Appointment> findAll() {
        return appointmentDAOImp.findAll();
    }

    @PostMapping(value = "/save")
    public Mono<Appointment> save(@RequestBody Appointment appointment) {
        appointment.setId(idGenerator.generateAppointmentID());
        return appointmentDAOImp.save(appointment);
    }
}

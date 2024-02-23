package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AppointmentComparable;
import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
        Mono<Appointment> data = appointmentDAOImp.save(appointment);
        return appointmentDAOImp.save(appointment);
    }

    @GetMapping(value = "/showAppointments")
    public List<Appointment> showAppointments() {
        Flux<Appointment> data = appointmentDAOImp.findAll();
        data.subscribe(appointment -> {
            AppointmentComparable appointmentComparable = new AppointmentComparable(appointment);
            DataLoader.getAppointments().insert(appointmentComparable);
        });
        ArrayList<Appointment> appointments = new ArrayList<>();
        DataLoader.getAppointments().forEach(e ->{
            appointments.add(e.getAppointment());
        });
        return appointments;
    }
}

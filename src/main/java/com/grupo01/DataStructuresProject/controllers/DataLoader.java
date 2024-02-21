package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AppointmentComparable;
import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
import com.grupo01.DataStructuresProject.datastructures.linkedlist.SortedLinkedList;
import com.grupo01.DataStructuresProject.models.Appointment;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DataLoader implements ApplicationRunner {
    @Getter
    private static SortedLinkedList<AppointmentComparable> appointments;
    private final AppointmentDAOImp appointmentDAOImp;

    @Autowired
    public DataLoader(AppointmentDAOImp appointmentDAOImp) {
        this.appointmentDAOImp = appointmentDAOImp;
        appointments = new SortedLinkedList<>();
    }

    public void load(){
        Flux<Appointment> data = appointmentDAOImp.findAll();
        data.subscribe(appointment -> {
            AppointmentComparable appointmentComparable = new AppointmentComparable(appointment);
            appointments.insert(appointmentComparable);
        });
    }

    @Override
    public void run(ApplicationArguments args) {
        load();
    }

}

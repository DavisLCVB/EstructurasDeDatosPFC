package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AppointmentDAOImp {

    @Autowired
    private AppointmentDAO appointmentDAO;

    public Flux<Appointment> findAllByIdPatient(String idPatient) {
        return appointmentDAO.findAllByIdPatient(idPatient);
    }

    public Flux<Appointment> findAllByIdProfessional(String idProfessional) {
        return appointmentDAO.findAllByIdProfessional(idProfessional);
    }

    public Flux<Appointment> findAllByIdArea(String idArea) {
        return appointmentDAO.findAllByIdArea(idArea);
    }

    public Flux<Appointment> findAllByIdPatientOrIdProfessional(String idPatient, String idProfessional) {
        return appointmentDAO.findAllByIdPatientOrIdProfessional(idPatient, idProfessional);
    }

    public Flux<Appointment> findAll() {
        return appointmentDAO.findAll();
    }

    public Mono<Void> deleteById(String id) {
        return appointmentDAO.deleteById(id);
    }

    public Mono<Appointment> save(Appointment appointment) {
        return appointmentDAO.save(appointment);
    }

    public Mono<Appointment> findById(String id) {
        return appointmentDAO.findById(id);
    }

    public Mono<Appointment> update(Appointment appointment) {
        return appointmentDAO.save(appointment);
    }
}

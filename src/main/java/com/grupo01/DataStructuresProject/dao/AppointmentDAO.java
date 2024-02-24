package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Appointment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AppointmentDAO extends ReactiveMongoRepository<Appointment, String> {
    Flux<Appointment> findAllByIdPatient(String idPatient);
    Flux<Appointment> findAllByIdProfessional(String idProfessional);
    Flux<Appointment> findAllByIdArea(String idArea);
    Flux<Appointment> findAllByIdPatientOrIdProfessional(String idPatient, String idProfessional);
}

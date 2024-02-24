package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.PatientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PatientDAOImp {
    @Autowired
    private PatientDAO patientDAO;

    public Flux<PatientUser> findAll() {
        return patientDAO.findAll();
    }

    public Mono<PatientUser> save(PatientUser patientUser) {
        return patientDAO.save(patientUser);
    }

    public Mono<PatientUser> findById(String id) {
        return patientDAO.findById(id);
    }

    public Mono<PatientUser> findByEmail(String email) {
        return patientDAO.findByEmail(email);
    }

    public Mono<PatientUser> findByEmailAndPassword(String email, String password) {
        return patientDAO.findByEmailAndPassword(email, password);
    }

    public Mono<Void> deleteById(String id) {
        return patientDAO.deleteById(id);
    }

    public Mono<Void> delete(PatientUser patientUser) {
        return patientDAO.delete(patientUser);
    }

    public Mono<Boolean> existsById(String id) {
        return patientDAO.existsById(id);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return patientDAO.existsByEmail(email);
    }

    public Mono<PatientUser> update(PatientUser patientUser) {
        return patientDAO.save(patientUser);
    }

}

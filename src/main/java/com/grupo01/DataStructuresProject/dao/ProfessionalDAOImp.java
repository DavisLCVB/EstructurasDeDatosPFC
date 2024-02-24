package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfessionalDAOImp {
    @Autowired
    private ProfessionalDAO professionalDAO;
    @Autowired
    private IDGenerator idGenerator;

    public Mono<ProfessionalUser> save(ProfessionalUser professionalUser) {
        professionalUser.setId(idGenerator.generateProfessionalID());
        return professionalDAO.save(professionalUser);
    }

    public Flux<ProfessionalUser> findAll() {
        return professionalDAO.findAll();
    }

    public Mono<ProfessionalUser> findById(String id) {
        return professionalDAO.findById(id);
    }

    public Flux<ProfessionalUser> findAllByIdArea(String idArea) {
        return professionalDAO.findAllByIdArea(idArea);
    }

    public Mono<ProfessionalUser> findByEmail(String email) {
        return professionalDAO.findByEmail(email);
    }

    public Mono<ProfessionalUser> findByEmailAndPassword(String email, String password) {
        return professionalDAO.findByEmailAndPassword(email, password);
    }

    public Mono<Void> deleteById(String id) {
        return professionalDAO.deleteById(id);
    }

    public Mono<Void> delete(ProfessionalUser professionalUser) {
        return professionalDAO.delete(professionalUser);
    }

    public Mono<Boolean> existsById(String id) {
        return professionalDAO.existsById(id);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return professionalDAO.existsByEmail(email);
    }

    public Mono<ProfessionalUser> update(String id, ProfessionalUser professionalUser) {
        return professionalDAO.findById(id).doOnNext(p -> {
            p.setFirstName(professionalUser.getFirstName());
            p.setLastName(professionalUser.getLastName());
            p.setEmail(professionalUser.getEmail());
            p.setPassword(professionalUser.getPassword());
            p.setAvailableHours(professionalUser.getAvailableHours());
            p.setIdArea(professionalUser.getIdArea());
        }).flatMap(professionalDAO::save);
    }
}

package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfessionalDAOImp {
    @Autowired
    private ProfessionalDAO professionalDAO;

    public Mono<ProfessionalUser> save(ProfessionalUser professionalUser) {
        return professionalDAO.save(professionalUser);
    }
    public Flux<ProfessionalUser> findAll(){
        return  professionalDAO.findAll();
    }

    public Mono<ProfessionalUser> findById(String id) {
        return professionalDAO.findById(id);
    }

    public Flux<ProfessionalUser> findAllByIdArea(String idArea){
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

    public Mono<ProfessionalUser> update(ProfessionalUser professionalUser) {
        return professionalDAO.save(professionalUser);
    }
}

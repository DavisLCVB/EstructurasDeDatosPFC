package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.PatientUser;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PatientDAO extends GenericDAO<PatientUser>{
    Mono<PatientUser> findByEmail(String email);
    Mono<Boolean> existsByEmail(String id);
    Mono<PatientUser> findByEmailAndPassword(String email, String password);
}


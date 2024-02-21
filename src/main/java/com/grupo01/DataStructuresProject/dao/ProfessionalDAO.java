package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfessionalDAO extends ReactiveMongoRepository<ProfessionalUser, String> {
    Mono<ProfessionalUser> findByEmail(String email);
    Mono<Boolean> existsByEmail(String id);
    Mono<ProfessionalUser> findByEmailAndPassword(String email, String password);
}

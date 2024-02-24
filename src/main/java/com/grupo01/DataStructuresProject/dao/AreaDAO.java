package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Area;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AreaDAO extends ReactiveMongoRepository<Area, String> {
    Mono<Area> findByName(String name);
}

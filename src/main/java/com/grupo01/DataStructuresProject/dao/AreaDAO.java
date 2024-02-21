package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Appointment;
import com.grupo01.DataStructuresProject.models.Area;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AreaDAO extends ReactiveMongoRepository<Area, String> {
    Flux<Area> findByName(String name);
}

package com.grupo01.DataStructuresProject.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericDAO<T> extends ReactiveMongoRepository<T, String> {
}

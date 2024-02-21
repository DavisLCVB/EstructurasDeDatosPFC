package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Area;
import reactor.core.publisher.Flux;

public interface AreaDAO extends GenericDAO<Area>{
    Flux<Area> findByName(String name);
}

package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.models.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AreaDAOImp {
    @Autowired
    private AreaDAO areaDAO;

    public Flux<Area> findAll() {
        return areaDAO.findAll();
    }

    public Mono<Area> findByName(String nombre) {
        return areaDAO.findByName(nombre);
    }

    public Mono<Area> findById(String id) {
        return areaDAO.findById(id);
    }

    public Mono<Area> save(Area area) {
        return areaDAO.save(area);
    }

    public Mono<Void> delete(Area area) {
        return areaDAO.delete(area);
    }

    public Mono<Void> deleteById(String id) {
        return areaDAO.deleteById(id);
    }

    public Mono<Area> update(Area area) {
        return areaDAO.save(area);
    }
}

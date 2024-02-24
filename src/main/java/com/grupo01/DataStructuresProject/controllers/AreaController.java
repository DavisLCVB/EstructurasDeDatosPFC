package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AreaDAOImp;
import com.grupo01.DataStructuresProject.models.Area;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/areas")
public class AreaController {
    @Autowired
    private AreaDAOImp areaDAOImp;
    @Autowired
    private IDGenerator idGenerator;

    @GetMapping(value = "/findAll")
    public Flux<Area> findAll() {
        return areaDAOImp.findAll();
    }

    @GetMapping(value = "/findById/{idArea}")
    public Mono<Area> findById(@PathVariable String idArea) {
        return areaDAOImp.findById(idArea);
    }

    @GetMapping(value = "/findByName/{name}")
    public Mono<Area> findByName(@PathVariable String name) {
        return areaDAOImp.findByName(name);
    }

    @PostMapping(value = "/save")
    public Mono<Area> save(@RequestBody Area area) {
        area.setId(idGenerator.generateAreaID());
        return areaDAOImp.save(area);
    }

    @DeleteMapping(value = "/delete/{idArea}")
    public Mono<Void> delete(@PathVariable String idArea) {
        return areaDAOImp.deleteById(idArea);
    }

    @PutMapping(value = "/update")
    public Mono<Area> update(@RequestBody Area area) {
        return areaDAOImp.update(area);
    }
}

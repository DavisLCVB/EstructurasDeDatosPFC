package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AreaDAOImp;
import com.grupo01.DataStructuresProject.frontformat.AreaFormat;
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
    public Flux<AreaFormat> findAll() {
        return areaDAOImp.findAll().map(AreaFormat::new);
    }

    @GetMapping(value = "/findById/{idArea}")
    public Mono<AreaFormat> findById(@PathVariable String idArea) {
        return areaDAOImp.findById(idArea).map(AreaFormat::new);
    }

    @GetMapping(value = "/findByName/{name}")
    public Mono<AreaFormat> findByName(@PathVariable String name) {
        return areaDAOImp.findByName(name).map(AreaFormat::new);
    }

    @PostMapping(value = "/save")
    public Mono<AreaFormat> save(@RequestBody Area area) {
        area.setId(idGenerator.generateAreaID());
        return areaDAOImp.save(area).map(AreaFormat::new);
    }

    @DeleteMapping(value = "/delete/{idArea}")
    public Mono<Void> delete(@PathVariable String idArea) {
        return areaDAOImp.deleteById(idArea);
    }

    @PutMapping(value = "/update")
    public Mono<AreaFormat> update(@RequestBody Area area) {
        return areaDAOImp.update(area).map(AreaFormat::new);
    }
}

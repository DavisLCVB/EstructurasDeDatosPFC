package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.PatientDAOImp;
import com.grupo01.DataStructuresProject.models.PatientUser;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientDAOImp patientDAOImp;
    @Autowired
    private IDGenerator idGenerator;

    @GetMapping(value = "/findAll")
    public Flux<PatientUser> findAll() {
        return patientDAOImp.findAll();
    }

    @GetMapping(value = "/findById/{id}")
    public Mono<PatientUser> findById(@PathVariable String id) {
        return patientDAOImp.findById(id);
    }

    @GetMapping(value = "/findByEmailAndPassword/{email}&{password}")
    public Mono<PatientUser> findByEmailAndPassword(@PathVariable String email, @PathVariable String password) {
        return patientDAOImp.findByEmailAndPassword(email, password);
    }

    @PostMapping(value = "/save")
    public Mono<PatientUser> save(@RequestBody PatientUser patient) {
        patient.setId(idGenerator.generatePatientID());
        return patientDAOImp.save(patient);
    }

    @PutMapping(value = "/update")
    public Mono<PatientUser> update(@RequestBody PatientUser patient) {
        return patientDAOImp.update(patient);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return patientDAOImp.deleteById(id);
    }
}

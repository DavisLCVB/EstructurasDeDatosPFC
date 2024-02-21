package com.grupo01.DataStructuresProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class IDGenerator {
    @Autowired
    private MongoOperations mongoOperations;

    public String generatePatientID() {
        String patientSequence = "PAT-";
        Query query = new Query(Criteria.where("id").is("patientSequence"));
        return getString(query, patientSequence);
    }

    public String generateProfessionalID() {
        String professionalSequence = "PRO-";
        Query query = new Query(Criteria.where("id").is("professionalSequence"));
        return getString(query, professionalSequence);
    }

    public String generateAppointmentID() {
        String appointmentSequence = "MED-";
        Query query = new Query(Criteria.where("id").is("appointmentSequence"));
        return getString(query, appointmentSequence);
    }

    public String generateAreaID() {
        String areaSequence = "AREA-";
        Query query = new Query(Criteria.where("id").is("areaSequence"));
        return getString(query, areaSequence);
    }

    private String getString(Query query, String sequence) {
        Update update = new Update().inc("seq", 1);
        DBSequence counter = mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), DBSequence.class);
        if (!Objects.isNull(counter)) {
            return sequence + counter.getSeq();
        } else {
            return sequence + 1;
        }
    }
}

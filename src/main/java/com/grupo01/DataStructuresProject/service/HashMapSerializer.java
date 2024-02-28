package com.grupo01.DataStructuresProject.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.grupo01.DataStructuresProject.datastructures.hashmap.HashMap;
import com.grupo01.DataStructuresProject.utils.ScheduleDate;

import java.io.IOException;

public class HashMapSerializer extends JsonSerializer<HashMap<String, ScheduleDate>> {
    @Override
    public void serialize(HashMap<String, ScheduleDate> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (int i = 0; i < value.getCapacity(); i++) {
            if (value.getTable()[i] != null) {
                for (int j = 0; j < value.getTable()[i].size(); j++) {
                    gen.writeFieldName(value.getTable()[i].get(j).getKey());
                    gen.writeObject(value.getTable()[i].get(j).getValue());
                }

            }
        }
        gen.writeEndObject();
    }
}

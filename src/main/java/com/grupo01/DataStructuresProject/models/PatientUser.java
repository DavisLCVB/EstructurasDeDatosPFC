package com.grupo01.DataStructuresProject.models;

import com.grupo01.DataStructuresProject.utils.Identify;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "patientUsers")
public class PatientUser extends GenericUser implements Identify {
}

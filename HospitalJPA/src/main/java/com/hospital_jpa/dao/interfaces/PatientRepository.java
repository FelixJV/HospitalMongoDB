package com.hospital_jpa.dao.interfaces;

import com.hospital_jpa.dao.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PatientRepository {
    List<Patient> getAll();
    String save(Patient patient);
    void update(Patient patient);
    void delete(String patientId);
}
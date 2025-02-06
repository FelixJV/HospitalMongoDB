package com.hospital_jpa.domain.service;

import com.hospital_jpa.dao.interfaces.PatientRepository;
import com.hospital_jpa.dao.interfaces.PaymentsRepository;
import com.hospital_jpa.dao.model.Credential;
import com.hospital_jpa.dao.model.Patient;
import com.hospital_jpa.dao.model.Payment;
import com.hospital_jpa.domain.model.PatientUI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientUI> getPatients() {
        List<Patient> patients = patientRepository.getAll();
        List<PatientUI> patientsUI = new ArrayList<>();

        patients.forEach(p -> {
            PatientUI patientUI = new PatientUI(p);
            if (p.getPayments() != null) {
                patientUI.setPaid((int) p.getPayments().stream().mapToDouble(Payment::getAmount).sum());
            }
            patientsUI.add(patientUI);
        });

        return patientsUI;
    }

    public String addPatient(PatientUI patientUI) {
        Patient patient = new Patient(
                null,  // MongoDB generará automáticamente un `_id`
                patientUI.getName(),
                patientUI.getBirthDate(),
                patientUI.getPhone(),
                new Credential(patientUI.getUserName(), patientUI.getPassword()),
                new ArrayList<>()
        );
        return patientRepository.save(patient);
    }

    public void updatePatient(PatientUI patientUI) {
        Patient patient = new Patient(
                String.valueOf(patientUI.getId()),
                patientUI.getName(),
                patientUI.getBirthDate(),
                patientUI.getPhone()
        );
        patientRepository.update(patient);
    }

    public void deletePatient(String patientId,boolean confirm) {
        if (confirm){
            patientRepository.delete(patientId);
        }

    }
}

package org.example.domain.services;

import org.example.dao.model.Medication;
import org.example.dao.repositories.MedicationRepository;
import org.example.domain.model.MedicationUI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }
    
    public List<MedicationUI> getAll() {
        List<Medication> medicationList = medicationRepository.findAll();
        List<MedicationUI> medicationUIList = new ArrayList<>();
        medicationList.forEach(medication -> medicationUIList.add(new MedicationUI(medication.getId(), medication.getMedicationName())));
        return medicationUIList;
    }
}

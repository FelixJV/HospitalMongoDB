package org.example.domain.services;

import org.example.dao.model.Doctor;
import org.example.dao.repositories.DoctorRepository;
import org.example.domain.model.DoctorUI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorUI> getAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorUI> doctorUIs = new ArrayList<>();
        doctors.forEach(doctor -> doctorUIs.add(new DoctorUI(doctor.getDoctorId(),doctor.getName(),doctor.getSpecialization())));
        return doctorUIs;
    }
}

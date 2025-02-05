package org.example.domain.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.model.*;
import org.example.dao.repositories.MedRecordRepository;
import org.example.dao.repositories.MedicationRepository;
import org.example.domain.model.MedRecordUI;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedRecordService {
    private final MedRecordRepository medRecordRepository;
    private final MedicationRepository medicationRepository;
    private final PatientService patientService;
    private final CredentialService credentialService;

    public MedRecordService(MedRecordRepository medRecordRepository, MedicationRepository medicationRepository, PatientService patientService, CredentialService credentialService) {
        this.medRecordRepository = medRecordRepository;
        this.medicationRepository = medicationRepository;
        this.patientService = patientService;
        this.credentialService = credentialService;
    }

    public List<MedRecordUI> getAll(int idPatient) {
        UserType user = credentialService.getAuthenticatedUser();

        if (user.getUserRole() == UserRole.PATIENT && user.getIdUser() != idPatient) {
            throw new SecurityException("Acceso denegado: No puedes ver el historial de otro paciente.");
        }

        List<MedRecord> medRecords = medRecordRepository.findByPatientId(idPatient);
        List<MedRecordUI> medRecordUIList = new ArrayList<>();

        medRecords.forEach(medRecord -> {
            List<Medication> medications = medicationRepository.findByMedRecordId(medRecord.getId());
            List<String> listMedications = medications.stream()
                    .map(Medication::getMedicationName)
                    .toList();

            medRecordUIList.add(new MedRecordUI(
                    medRecord.getId(),
                    medRecord.getDiagnosis(),
                    medRecord.getDate().toString(),
                    medRecord.getPatient().getId(),
                    medRecord.getIdDoctor(),
                    listMedications
            ));
        });

        return medRecordUIList;
    }

    public int save(MedRecordUI medRecordui) {
        UserType user = credentialService.getAuthenticatedUser();

        if (user.getUserRole() == UserRole.DOCTOR) {
            int doctorId = credentialService.getDoctorIdByCredentialId(user.getIdUser());
            if(doctorId != medRecordui.getIdDoctor()){
                throw new SecurityException("Acceso denegado: No puedes eliminar registros médicos de pacientes que no son tuyos.");
            }
        }

        if (user.getUserRole() == UserRole.DOCTOR && user.getIdUser() != medRecordui.getIdDoctor()) {
            throw new SecurityException("Acceso denegado: No puedes agregar registros médicos a pacientes que no son tuyos.");
        }

        List<Medication> medications = new ArrayList<>();
        LocalDate date = LocalDate.parse(medRecordui.getDate().toString());

        medRecordui.getMedications().forEach(medication ->
                medications.add(new Medication(medication, medRecordui.getId())));

        MedRecord medRecord = new MedRecord(medRecordui.getId(), new Patient(medRecordui.getIdPatient()),
                medRecordui.getIdDoctor(), medRecordui.getDescription(), date, medications);

        medRecord.getMedications().forEach(medication -> medication.setMedRecord(medRecord));
        return medRecordRepository.save(medRecord).getId();
    }

    public void delete(int id) {
        UserType user = credentialService.getAuthenticatedUser();

        Optional<MedRecord> medRecordOptional = medRecordRepository.findById(id);
        if (medRecordOptional.isEmpty()) {
            throw new EntityNotFoundException("Registro médico no encontrado.");
        }

        MedRecord medRecord = medRecordOptional.get();

        if (user.getUserRole() == UserRole.DOCTOR) {
            int doctorId = credentialService.getDoctorIdByCredentialId(user.getIdUser());
            if(doctorId != medRecord.getIdDoctor()){
                throw new SecurityException("Acceso denegado: No puedes eliminar registros médicos de pacientes que no son tuyos.");
            }
        }

        if (user.getUserRole() == UserRole.PATIENT) {
            throw new SecurityException("Acceso denegado: No puedes eliminar tu historial médico.");
        }

        medRecordRepository.deleteById(id);
    }

    public void update(MedRecordUI medRecordui) {
        UserType user = credentialService.getAuthenticatedUser();

        Optional<MedRecord> medRecordOptional = medRecordRepository.findById(medRecordui.getId());
        if (medRecordOptional.isEmpty()) {
            throw new EntityNotFoundException("Registro médico no encontrado.");
        }

        MedRecord medRecord = medRecordOptional.get();

        if (user.getUserRole() == UserRole.DOCTOR) {
            int doctorId = credentialService.getDoctorIdByCredentialId(user.getIdUser());
            if(doctorId != medRecord.getIdDoctor()){
                throw new SecurityException("Acceso denegado: No puedes eliminar registros médicos de pacientes que no son tuyos.");
            }
        }

        if (user.getUserRole() == UserRole.PATIENT) {
            throw new SecurityException("Acceso denegado: No puedes modificar tu historial médico.");
        }

        List<Medication> medications = new ArrayList<>();
        LocalDate date = LocalDate.parse(medRecordui.getDate().toString());

        medRecordui.getMedications().forEach(medication ->
                medications.add(new Medication(medication, medRecordui.getId())));

        MedRecord updatedMedRecord = new MedRecord(medRecordui.getId(), new Patient(medRecordui.getIdPatient()),
                medRecordui.getIdDoctor(), medRecordui.getDescription(), date, medications);

        medicationRepository.removeByMedRecordId(medRecordui.getId());
        medRecordRepository.save(updatedMedRecord);
    }
}

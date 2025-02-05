package org.example.domain.services;

import org.example.dao.model.*;
import org.example.dao.repositories.AppointmentRepository;
import org.example.dao.repositories.MedRecordRepository;
import org.example.dao.repositories.PatientRepository;
import org.example.dao.repositories.PaymentsRepository;
import org.example.domain.errors.ForeignKeyException;
import org.example.domain.model.PatientUI;
import org.example.ui.errors.GlobalExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PaymentsRepository paymentsRepository;
    private final MedRecordRepository medRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final CredentialService credentialService;

    public PatientService(PatientRepository patientRepository, PaymentsRepository paymentsRepository, MedRecordRepository medRecordRepository, AppointmentRepository appointmentRepository, CredentialService credentialService) {
        this.patientRepository = patientRepository;
        this.paymentsRepository = paymentsRepository;
        this.medRecordRepository = medRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.credentialService = credentialService;
    }

    public List<PatientUI> getAll() {
        UserType user = credentialService.getAuthenticatedUser();
        List<Patient> patients;

        if (user.getUserRole() == UserRole.DOCTOR || user.getUserRole() == UserRole.ADMIN) {
            patients = patientRepository.findAll();
        } else if (user.getUserRole() == UserRole.PATIENT) {
            Optional<Patient> patient = patientRepository.findById(user.getIdUser());
            if (patient.isPresent()) {
                patients = List.of(patient.get());
            } else {
                throw new SecurityException("Acceso denegado: No tienes permisos para ver esta información.");
            }
        } else {
            throw new SecurityException("Acceso denegado: Rol no reconocido.");
        }

        List<Payment> paymentList = paymentsRepository.findAll();
        List<PatientUI> patientsUI = new ArrayList<>();
        patients.forEach(patient -> {
            double paidAmount = paymentList.stream()
                    .filter(payment -> payment.getPatient().getId() == patient.getId())
                    .mapToDouble(Payment::getAmount)
                    .findFirst()
                    .orElse(0.0);
            patientsUI.add(new PatientUI(patient.getId(), patient.getName(), null,
                    patient.getBirthDate(), patient.getPhone(), paidAmount, null));
        });

        return patientsUI;
    }

    public int save(PatientUI patientUI) {
        UserType user = credentialService.getAuthenticatedUser();
        if (user.getUserRole() != UserRole.DOCTOR && user.getUserRole() != UserRole.ADMIN) {
            throw new SecurityException("Acceso denegado: Solo los doctores y administradores pueden agregar pacientes.");
        }

        patientRepository.flush();
        Credential credential = new Credential(0, patientUI.getUserName(), patientUI.getPassword(), new Patient(), null);
        Patient patient = new Patient(patientUI.getId(), patientUI.getName(), patientUI.getBirthDate(), patientUI.getPhone());
        patient.setCredential(credential);
        return patientRepository.save(patient).getId();
    }

    public void update(PatientUI patientUI) {
        UserType user = credentialService.getAuthenticatedUser();
        if (user.getUserRole() == UserRole.PATIENT && user.getIdUser() != patientUI.getId()) {
            throw new SecurityException("Acceso denegado: No puedes actualizar información de otro paciente.");
        }

        if (user.getUserRole() != UserRole.PATIENT && user.getUserRole() != UserRole.DOCTOR && user.getUserRole() != UserRole.ADMIN) {
            throw new SecurityException("Acceso denegado: No tienes permisos para actualizar pacientes.");
        }

        Patient patient = new Patient(patientUI.getId(), patientUI.getName(), patientUI.getBirthDate(), patientUI.getPhone());
        patientRepository.save(patient);
    }

    public void delete(int patientId, boolean confirm) {
        UserType user = credentialService.getAuthenticatedUser();
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new SecurityException("Acceso denegado: Solo el administrador puede eliminar pacientes.");
        }

        Patient patient = new Patient(patientId, null, null, null);
        try {
            if (confirm) {
                medRecordRepository.deleteByPatientId(patientId);
                paymentsRepository.deleteByPatientId(patientId);
                appointmentRepository.deleteByPatientId(patientId);
            }
            patientRepository.findById(patientId).ifPresent(patientRepository::delete);
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException("Error al eliminar el paciente con ID: " + patient.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ForeignKeyException("Error al eliminar el paciente con ID: " + patient.getId());
        }
    }
}

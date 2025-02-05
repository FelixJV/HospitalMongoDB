package org.example.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.model.Credential;
import org.example.dao.model.Doctor;
import org.example.dao.model.UserRole;
import org.example.dao.model.UserType;
import org.example.dao.repositories.CredentialRepository;
import org.example.domain.model.CredentialUI;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public boolean get(CredentialUI credentialui) {
        Credential credentialR = this.credentialRepository.findByUsername(credentialui.getUsername());
        if (credentialR == null) {
            return false;
        }
        authenticate(credentialR.getUsername(),credentialR.getPassword());
        return Objects.equals(credentialR.getPassword(), credentialui.getPassword());
    }

    public UserType authenticate(String username, String password) {
        Credential credential = credentialRepository.findByUsername(username);

        if (credential == null || !credential.getPassword().equals(password)) {
            return null;
        }

        UserRole role;
        if (credential.getPatient() != null) {
            role = UserRole.PATIENT;
        } else if (credential.getDoctorId() != null) {
            role = UserRole.DOCTOR;
        } else {
            role = UserRole.ADMIN;
        }

        UserType userType = new UserType(credential.getUserId(), role);

        saveUserTypeToJson(userType);

        return userType;
    }

    private void saveUserTypeToJson(UserType userType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("user_type.json"), userType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static UserType getAuthenticatedUser() {
        try {
            return objectMapper.readValue(new File("user_type.json"), UserType.class);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el usuario autenticado", e);
        }
    }
    public static boolean isAdmin() {
        return getAuthenticatedUser().getUserRole() == UserRole.ADMIN;
    }
    public static boolean isDoctor() {
        return getAuthenticatedUser().getUserRole() == UserRole.DOCTOR;
    }
    public static boolean isPatient() {
        return getAuthenticatedUser().getUserRole() == UserRole.PATIENT;
    }

    public static boolean isSamePatient(int patientId) {
        UserType user = getAuthenticatedUser();
        return user.getUserRole() == UserRole.PATIENT && user.getIdUser() == patientId;
    }

    public static boolean isSameDoctor(int doctorId) {
        UserType user = getAuthenticatedUser();
        return user.getUserRole() == UserRole.DOCTOR && user.getIdUser() == doctorId;
    }

    public int getDoctorIdByCredentialId(int idUser) {
        int idDoctor = 0;
        Optional<Credential> credentialR = this.credentialRepository.findById(idUser);
        if(credentialR.isPresent()){
            idDoctor = credentialR.get().getDoctorId();
        }

        return idDoctor;
    }
}

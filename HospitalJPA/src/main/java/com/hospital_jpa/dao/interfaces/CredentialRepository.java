package com.hospital_jpa.dao.interfaces;

import com.hospital_jpa.dao.model.Credential;

import java.util.List;

public interface CredentialRepository {
    List<Credential> getAll();
    boolean delete(String patientId);

    Credential get(String username);
}


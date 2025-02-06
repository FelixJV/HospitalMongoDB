package com.hospital_jpa.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "patients")
public class Patient {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String phone;
    private Credential credential;

    private List<Payment> payments;

    public Patient(String id, String name, LocalDate birthDate, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    public Patient(String id, String name, LocalDate birthDate, String phone, Credential credential) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.credential = credential;
    }

    public Patient(String id) {
        this.id = id;
    }

    public Patient(String id, String name, String dateOfBirth, String phone) {
    }

    public <E> Patient(Object o, String name, LocalDate birthDate, String phone, Credential credential, ArrayList<E> es) {
    }
}

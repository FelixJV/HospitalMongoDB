package org.example.dao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
@NamedQuery(name = "HQL_GET_ALL_PATIENTS",
        query = "from Patient ")
public class Patient {
    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "date_of_birth")
    private LocalDate birthDate;
    @Column
    private String phone;
    @OneToOne(mappedBy = "patient", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Credential credential;

    public Patient(int id, String name, LocalDate dob, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = dob;
        this.phone = phone;
    }

    public Patient(int id) {
        this.id = id;
    }

    public void  setCredential(Credential credential){
        this.credential = credential;
        credential.setPatient(this);
    }
}

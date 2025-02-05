package org.example.dao.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "doctors")
@NamedQuery(name = "HQL_GET_ALL_DOCTORS",
        query = "from Doctor")
public class Doctor {
    @Id
    @Column(name = "doctor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;
    @Column
    private String name;
    @Column
    private String specialization;
    @Column
    private String phone;
}

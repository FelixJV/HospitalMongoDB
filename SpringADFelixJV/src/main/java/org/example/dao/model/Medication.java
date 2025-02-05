package org.example.dao.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prescribed_medications")
@NamedQuery(name = "HQL_GET_MEDS",
        query = "from Medication where medRecord.id = ?1")
public class Medication {
    @Id
    @Column(name = "prescription_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "medication_name")
    private String medicationName;
    @Column(name = "dosage")
    private String dosage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private MedRecord medRecord;


    public Medication(String m,int medRecord) {
        this.medicationName = m;
        this.medRecord = new MedRecord(medRecord) ;
    }
}

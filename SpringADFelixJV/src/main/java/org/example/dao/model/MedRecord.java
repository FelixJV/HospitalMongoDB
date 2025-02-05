    package org.example.dao.model;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;
    import java.util.List;

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @Entity
    @Table(name = "medical_records")
    @NamedQuery(name = "MedRecord.getAllByIdPatient", query = "SELECT m FROM MedRecord m WHERE m.patient.id = :idPatient")
    @NamedQuery(name = "DELETE_MEDS",
                    query = "delete from Medication m where m.medRecord.id = ?1")

    public class MedRecord {
        @Id
        @Column(name = "record_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id")
        private Patient patient;
        @Column(name = "doctor_id")
        private int idDoctor;
        @Column(name = "diagnosis")
        private String diagnosis;
        @Column(name = "admission_date")
        private LocalDate date;
        @OneToMany(mappedBy = "medRecord", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
        private List<Medication> medications;

        public MedRecord(int id, Patient patient, int idDoctor, String diagnosis, LocalDate date, List<Medication> medications) {
            this.id = id;
            this.patient = patient;
            this.idDoctor = idDoctor;
            this.diagnosis = diagnosis;
            this.date = date;
            this.medications = medications;
        }

        public MedRecord(int id) {
            this.id = id;
        }

        public void addMedication() {
            this.medications = medications;
        }

        public void setDescription(String description) {
        }

        public void setDoctorId(int idDoctor) {
        }
    }

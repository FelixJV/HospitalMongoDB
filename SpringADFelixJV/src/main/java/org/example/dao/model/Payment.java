package org.example.dao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patient_payments")
@NamedQuery(name = "HQL_GET_ALL_PAYMENTS",
        query = "from Payment")
public class Payment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "payment_id")
        private int id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id")
        private Patient patient;

        @Column(name = "amount")
        private double amount;

        @Column(name = "payment_date")
        private LocalDate paymentDate;
}

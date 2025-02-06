package com.hospital_jpa.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor

public class Payment {

    private int amount;
    private LocalDate date;


    public Payment(int amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }
}
package org.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientUI {
    private int id;
    private String name;
    private String userName;
    private LocalDate birthDate;
    private String phone;
    private double paid;
    private String password;

}

package org.example.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorUI {
    private int id;
    private String name;
    private String speciality;
}

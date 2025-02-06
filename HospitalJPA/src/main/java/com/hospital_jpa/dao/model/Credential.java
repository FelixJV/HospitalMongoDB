package com.hospital_jpa.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_login")
public class Credential {
    private String id;
    private String username;
    private String password;
    private String patientId;
    private String doctorId;

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
        this.doctorId = null;
    }

    public Credential(String id, String username, String password, String patientId) {
    }
}

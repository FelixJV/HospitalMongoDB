package org.example.ui;

import org.example.domain.model.MedicationUI;
import org.example.domain.services.MedicationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestMedication {
    private final MedicationService medService;

    public RestMedication(MedicationService medService) {
        this.medService = medService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/medications")
    public List<MedicationUI> index() {
        return medService.getAll();
    }
}

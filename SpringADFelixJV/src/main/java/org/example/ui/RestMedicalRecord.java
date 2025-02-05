package org.example.ui;

import org.example.domain.model.MedRecordUI;
import org.example.domain.services.MedRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class RestMedicalRecord {
    private final MedRecordService medRecordService;

    public RestMedicalRecord(MedRecordService medRecordService) {
        this.medRecordService = medRecordService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/patients/{idPatient}/medRecords")
    public List<MedRecordUI> getAllMedRecord(@PathVariable int idPatient) {
        return medRecordService.getAll(idPatient);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/patients/medRecords")
    public int addMedRecord(@RequestBody MedRecordUI medRecordui) {
        return medRecordService.save(medRecordui);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @DeleteMapping("/patients/medRecords/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedRecord(@PathVariable int id) {
        medRecordService.delete(id);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PutMapping("/patients/medRecords")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMedRecord(@RequestBody MedRecordUI medRecordUI) {
        medRecordService.update(medRecordUI);
    }
}

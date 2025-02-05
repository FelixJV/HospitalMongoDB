package org.example.dao.repositories;

import org.example.dao.model.MedRecord;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedRecordRepository extends JpaRepository<MedRecord,Integer> {
    List<MedRecord> findByPatientId(int patientId);
    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    void deleteByPatientId(int patient_id);
}

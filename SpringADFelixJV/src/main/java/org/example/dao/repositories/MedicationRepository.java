package org.example.dao.repositories;

import org.example.dao.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Integer> {
    List<Medication> findByMedRecordId(int medRecordId);
    void removeByMedRecordId(int medRecordId);
}

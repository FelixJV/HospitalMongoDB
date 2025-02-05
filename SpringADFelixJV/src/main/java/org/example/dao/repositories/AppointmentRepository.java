package org.example.dao.repositories;

import org.example.dao.model.Appointment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    void deleteByPatientId(int patientId);

}

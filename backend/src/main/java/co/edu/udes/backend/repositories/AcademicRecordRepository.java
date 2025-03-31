package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {
    ScopedValue<AcademicRecord> findByStudentId(Long studentId);
}

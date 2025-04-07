package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.AcademicRecord;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {
    boolean existsByStudentAndSubject(Student student, Subject subject);
    Optional<AcademicRecord> findByStudentAndSubject(Student student, Subject subject);
    List<AcademicRecord> findByStudent(Student student);
}

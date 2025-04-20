package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<Grade> findByPeriodId(Long periodId);

    Grade findByStudentIdAndSubjectIdAndPeriodId(Long studentId, Long subjectId, Long periodId);
}
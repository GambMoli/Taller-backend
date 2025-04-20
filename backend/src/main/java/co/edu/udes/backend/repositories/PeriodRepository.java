package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findBySemesterId(Long semesterId);

    List<Period> findBySemesterIdIn(List<Long> semesterIds);

    void deleteBySemesterId(Long semesterId);

    Arrays findByStudentIdOrderByStartDateAsc(Long studentId);

    boolean existsByNameAndStudentId(String name, Long studentId);

    void deleteByStudentId(Long studentId);

    boolean existsByStudentIdAndSemesterId(Long studentId, Long semesterId);
}

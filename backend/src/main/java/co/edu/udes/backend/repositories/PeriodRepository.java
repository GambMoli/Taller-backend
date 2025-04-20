package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findBySemesterIdOrderByStartDateAsc(Long semesterId);
    void deleteBySemesterId(Long semesterId);
}
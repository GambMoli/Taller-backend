package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    boolean existsByNumberAndCareerId(Integer number, Long careerId);

    List<Semester> findByCareerIdOrderByNumberAsc(Long careerId);
}
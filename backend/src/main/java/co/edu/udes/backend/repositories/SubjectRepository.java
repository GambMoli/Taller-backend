package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Subject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySemesterId(Long semesterId);

    @EntityGraph(attributePaths = {"careers", "prerequisite"})
    @Query("SELECT s FROM Subject s")
    List<Subject> findAllWithCareersAndPrerequisite();

    boolean existsByName(String name);

    List<Subject> findByCareers_Id(Long careerId);

    @Query("SELECT s FROM Subject s LEFT JOIN FETCH s.semester WHERE s.id = :id")
    Optional<Subject> findByIdWithSemester(@Param("id") Long id);

}

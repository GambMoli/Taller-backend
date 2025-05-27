package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByEmail(String email);
    Optional<Teacher> findByEmail(String email);
}
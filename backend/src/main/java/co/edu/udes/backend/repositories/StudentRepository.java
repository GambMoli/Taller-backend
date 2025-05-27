package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
}
package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

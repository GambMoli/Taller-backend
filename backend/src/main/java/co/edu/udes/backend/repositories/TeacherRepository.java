package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}

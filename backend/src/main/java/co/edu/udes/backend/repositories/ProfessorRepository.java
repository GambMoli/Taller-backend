package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}

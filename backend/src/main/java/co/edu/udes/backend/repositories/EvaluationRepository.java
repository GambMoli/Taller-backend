package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Evaluation;
import co.edu.udes.backend.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    boolean existsByName(String name);

}

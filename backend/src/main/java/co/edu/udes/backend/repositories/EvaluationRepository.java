package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}

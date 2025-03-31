package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Calification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificationRepository extends JpaRepository<Calification, Long> {
}

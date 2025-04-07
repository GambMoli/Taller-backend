package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long> {
    boolean existsByName(String name);
}

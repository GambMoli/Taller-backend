package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}

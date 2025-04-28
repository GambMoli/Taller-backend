package co.edu.udes.backend.repositories;

import co.edu.udes.backend.dto.material.MaterialDTO;
import co.edu.udes.backend.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material,Long> {
 boolean existsByCode(String code);

}

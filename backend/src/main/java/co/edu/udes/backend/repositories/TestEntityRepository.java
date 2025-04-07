package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
}

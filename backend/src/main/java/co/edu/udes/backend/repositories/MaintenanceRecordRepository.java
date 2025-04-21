package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord,Long> {
    boolean existsByCode(String code);
}

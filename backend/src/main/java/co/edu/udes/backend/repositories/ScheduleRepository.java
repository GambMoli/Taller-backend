package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

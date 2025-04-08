package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Set<Schedule> findByGroupId(Long groupId);
}
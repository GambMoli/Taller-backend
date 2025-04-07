package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}

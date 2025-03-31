package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
}

package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

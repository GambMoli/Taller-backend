package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.participant1.id = :studentId OR c.participant2.id = :studentId")
    List<Chat> findByParticipantId(Long studentId);

}
package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE " +
            "(c.participant1.id = :participant1Id AND c.participant2.id = :participant2Id) OR " +
            "(c.participant1.id = :participant2Id AND c.participant2.id = :participant1Id)")
    Optional<Chat> findExistingChat(Long participant1Id, Long participant2Id);

    @Query("SELECT c FROM Chat c WHERE c.participant1.id = :studentId OR c.participant2.id = :studentId")
    List<Chat> findByParticipantId(Long studentId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Chat c WHERE c.id = :chatId AND " +
            "(c.participant1.id = :studentId OR c.participant2.id = :studentId)")
    boolean isStudentInChat(Long chatId, Long studentId);
}
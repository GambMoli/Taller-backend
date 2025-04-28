package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.date ASC")
    List<Message> findByChatIdOrderByDateAsc(@Param("chatId") Long chatId);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :senderId ORDER BY m.date DESC")
    List<Message> findBySenderIdOrderByDateDesc(@Param("senderId") Long senderId);
}
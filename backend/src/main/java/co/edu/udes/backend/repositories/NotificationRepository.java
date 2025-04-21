package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByGroupClassIdOrderByCreatedAtDesc(Long groupClassId);
    List<Notification> findBySenderIdOrderByCreatedAtDesc(Long teacherId);
    List<Notification> findByGroupClassIdAndIsReadOrderByCreatedAtDesc(Long groupClassId, boolean isRead);
    long countByGroupClassIdAndIsRead(Long groupClassId, boolean isRead);
}
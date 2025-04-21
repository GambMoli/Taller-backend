package co.edu.udes.backend.dto.notification;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private boolean isRead;
    private Long senderId;
    private String senderName;
    private Long groupClassId;
    private String groupClassName;
}
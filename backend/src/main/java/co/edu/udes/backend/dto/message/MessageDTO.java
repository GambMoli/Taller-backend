package co.edu.udes.backend.dto.message;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime date;
    private Long senderId;
    private String senderName;
    private Long chatId;
}
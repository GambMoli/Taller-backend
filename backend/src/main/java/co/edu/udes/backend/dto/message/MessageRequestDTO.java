package co.edu.udes.backend.dto.message;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private Long senderId;
    private Long chatId;
    private String title;
    private String body;
}
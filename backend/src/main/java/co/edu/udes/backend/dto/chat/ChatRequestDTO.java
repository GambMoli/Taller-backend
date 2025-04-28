package co.edu.udes.backend.dto.chat;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private Long participant1Id;
    private Long participant2Id;
}
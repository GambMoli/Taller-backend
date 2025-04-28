package co.edu.udes.backend.dto.chat;

import co.edu.udes.backend.dto.message.MessageDTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatDTO {
    private Long id;
    private LocalDateTime creationDate;
    private Long participant1Id;
    private String participant1Name;
    private Long participant2Id;
    private String participant2Name;
    private List<MessageDTO> messages;
}
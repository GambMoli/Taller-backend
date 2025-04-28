package co.edu.udes.backend.dto.forum;

import co.edu.udes.backend.dto.message.MessageDTO;
import lombok.Data;

import java.util.List;

@Data
public class ForumDTO {
    private Long id;
    private String topic;
    private Long groupClassId;
    private String groupClassName;
    private List<MessageDTO> messages;
}

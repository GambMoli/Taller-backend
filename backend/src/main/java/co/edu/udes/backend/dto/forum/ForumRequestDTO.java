package co.edu.udes.backend.dto.forum;

import lombok.Data;

@Data
public class ForumRequestDTO {
    private String topic;
    private Long groupId;
}

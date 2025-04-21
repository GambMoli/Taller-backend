package co.edu.udes.backend.dto.notification;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private String title;
    private String content;
    private Long teacherId;
    private Long groupClassId;
}

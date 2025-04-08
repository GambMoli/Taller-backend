package co.edu.udes.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleDTO {
    private Long groupId;
    private String subjectName;
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;
}

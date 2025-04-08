package co.edu.udes.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;
    private Long groupId;

}
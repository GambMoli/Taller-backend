package co.edu.udes.backend.dto.schedule;

import co.edu.udes.backend.models.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Long id;
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;

    public static ScheduleResponseDTO fromEntity(Schedule schedule) {
        ScheduleResponseDTO dto = new ScheduleResponseDTO();
        dto.setId(schedule.getId());
        dto.setDay(schedule.getDay());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setClassroom(schedule.getClassroom());
        return dto;
    }
}
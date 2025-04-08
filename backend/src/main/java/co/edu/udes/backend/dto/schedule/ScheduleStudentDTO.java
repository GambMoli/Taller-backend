package co.edu.udes.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStudentDTO {
    private Long studentId;
    private String studentName;
    private List<ClassScheduleDTO> schedules;
}
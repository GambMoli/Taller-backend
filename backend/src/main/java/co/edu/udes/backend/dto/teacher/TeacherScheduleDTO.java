package co.edu.udes.backend.dto.teacher;

import co.edu.udes.backend.dto.schedule.ClassScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherScheduleDTO {
    private Long teacherId;
    private String teacherName;
    private Integer workloadHours;
    private Integer assignedHours;
    private Integer availableHours;
    private List<ClassScheduleDTO> schedules;
}
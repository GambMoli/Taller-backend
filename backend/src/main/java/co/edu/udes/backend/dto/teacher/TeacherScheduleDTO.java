package co.edu.udes.backend.dto.teacher;

import co.edu.udes.backend.dto.schedule.ClassScheduleDTO;
import co.edu.udes.backend.dto.schedule.DayScheduleDTO;
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
    private String teacherEmail;
    private int assignedHours;
    private List<DayScheduleDTO> weekSchedule; // Array de días, donde cada posición es un día
}
package co.edu.udes.backend.dto.groups;

import co.edu.udes.backend.dto.schedule.ScheduleResponseDTO;
import co.edu.udes.backend.dto.student.StudentResponseDTO;
import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Schedule;
import co.edu.udes.backend.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupClassResponseDTO {
    private Long id;
    private String name;
    private int capacity;
    private int enrolledCount;
    private Long subjectId;
    private String subjectName;
    private Set<StudentResponseDTO> students;
    private Set<ScheduleResponseDTO> schedules;

    public static GroupClassResponseDTO fromEntity(GroupClass groupClass) {
        GroupClassResponseDTO dto = new GroupClassResponseDTO();
        dto.setId(groupClass.getId());
        dto.setName(groupClass.getName());
        dto.setCapacity(groupClass.getCapacity());
        dto.setEnrolledCount(groupClass.getEnrolledCount());

        if (groupClass.getSubject() != null) {
            dto.setSubjectId(groupClass.getSubject().getId());
            dto.setSubjectName(groupClass.getSubject().getName());
        }

        // Mapear estudiantes matriculados
        Set<StudentResponseDTO> studentDTOs = groupClass.getStudents().stream()
                .map(StudentResponseDTO::fromEntity)
                .collect(Collectors.toSet());
        dto.setStudents(studentDTOs);

        // Mapear horarios
        Set<ScheduleResponseDTO> scheduleDTOs = groupClass.getSchedules().stream()
                .map(ScheduleResponseDTO::fromEntity)
                .collect(Collectors.toSet());
        dto.setSchedules(scheduleDTOs);

        return dto;
    }
}
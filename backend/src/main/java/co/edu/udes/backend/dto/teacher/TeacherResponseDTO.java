package co.edu.udes.backend.dto.teacher;

import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Integer workloadHours;
    private Integer assignedHours;
    private Integer availableHours;
}

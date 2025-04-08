package co.edu.udes.backend.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupAssignmentDTO {
    private Long teacherId;
    private Long groupId;
}

package co.edu.udes.backend.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private String name;
    private String email;
    private String password;
    private Integer workloadHours;
}

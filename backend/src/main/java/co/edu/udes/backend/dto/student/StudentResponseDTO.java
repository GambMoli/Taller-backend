package co.edu.udes.backend.dto.student;

import co.edu.udes.backend.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String email;
    private Long careerId;
    private String careerName;

    public static StudentResponseDTO fromEntity(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setCode(student.getCode());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());

        if (student.getCareer() != null) {
            dto.setCareerId(student.getCareer().getId());
            dto.setCareerName(student.getCareer().getName());
        }

        return dto;
    }
}
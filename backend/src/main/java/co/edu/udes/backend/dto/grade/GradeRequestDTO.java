package co.edu.udes.backend.dto.grade;

import lombok.Data;

@Data
public class GradeRequestDTO {
    private Long studentId;
    private Long subjectId;
    private Long periodId;
    private Double value;
}
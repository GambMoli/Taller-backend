package co.edu.udes.backend.dto.grade;

import lombok.Data;

@Data
public class GradeCreateDTO {
    private Double value;
    private Long studentId;
    private Long subjectId;
    private Long periodId;
    private Long evaluationId;
}

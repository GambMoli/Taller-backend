package co.edu.udes.backend.dto.grade;

import lombok.Data;

@Data
public class GradeResponseDTO {
    private Long id;
    private Double value;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private Long periodId;
    private String periodName;
    private Long evaluationId;
    private String evaluationName;
}
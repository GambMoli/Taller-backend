package co.edu.udes.backend.dto.grade;

import lombok.Data;

@Data
public class FinalGradeDTO {
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private Double finalGrade;
    private Boolean passed;
}
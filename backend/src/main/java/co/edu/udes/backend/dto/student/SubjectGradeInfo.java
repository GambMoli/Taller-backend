package co.edu.udes.backend.dto.student;

import lombok.Data;

@Data
public class SubjectGradeInfo {
    private Long subjectId;
    private String subjectName;
    private Integer credits;
    private Double grade;
    private boolean approved;
}
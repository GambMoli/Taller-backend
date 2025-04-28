package co.edu.udes.backend.dto.period;

import lombok.Data;

@Data
public class SubjectGradeDTO {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private Double grade;
    private boolean approved;
    private Double noteStudent;
}
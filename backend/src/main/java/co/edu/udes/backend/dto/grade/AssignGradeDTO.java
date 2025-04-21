package co.edu.udes.backend.dto.grade;

import lombok.Data;

@Data
public class AssignGradeDTO {
    private Long periodId;
    private Long subjectId;
    private Double value;
}

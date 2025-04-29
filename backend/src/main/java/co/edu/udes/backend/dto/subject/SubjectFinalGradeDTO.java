package co.edu.udes.backend.dto.subject;

import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectFinalGradeDTO {
    private Long subjectId;
    private String subjectName;
    private List<PeriodGradeDTO> periodGrades;
    private double finalGrade;
    private boolean approved;
}
package co.edu.udes.backend.dto.period;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class PeriodResponseDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double weight;
    private Long studentId;
    private String studentName; // Añade esta propiedad
    private List<SubjectGradeDTO> subjectGrades;
    private Double finalGrade;
    private Boolean approved;
}

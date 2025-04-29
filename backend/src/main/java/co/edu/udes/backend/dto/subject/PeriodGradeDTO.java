package co.edu.udes.backend.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodGradeDTO {
    private Long periodId;
    private String periodName;
    private double grade;
    private double weight;
}

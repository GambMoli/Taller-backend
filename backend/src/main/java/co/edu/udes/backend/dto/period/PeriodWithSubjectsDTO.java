package co.edu.udes.backend.dto.period;

import lombok.Data;

import java.util.Map;

@Data
public class PeriodWithSubjectsDTO {
    private Long periodId;
    private String namePeriod;
    private Double weight;
    private Map<String, SubjectGradeDTO> subjects;
}
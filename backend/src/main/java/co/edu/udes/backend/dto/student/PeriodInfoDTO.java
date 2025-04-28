package co.edu.udes.backend.dto.student;

import lombok.Data;

import java.util.Map;

@Data
public class PeriodInfoDTO {
    private Long periodId;
    private String periodName;
    private Double weight;
    private Map<String, SubjectGradeInfo> subjects;
}

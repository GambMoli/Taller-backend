package co.edu.udes.backend.dto.student;

import lombok.Data;

import java.util.List;

@Data
public class SemesterInfoDTO {
    private Long semesterId;
    private Integer semesterNumber;
    private String semesterName;
    private List<PeriodInfoDTO> periods;
    private Double finalGrade;
    private boolean approved;
}


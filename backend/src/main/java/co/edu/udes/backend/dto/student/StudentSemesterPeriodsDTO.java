package co.edu.udes.backend.dto.student;

import co.edu.udes.backend.dto.period.PeriodWithSubjectsDTO;
import lombok.Data;

import java.util.List;

@Data
public class StudentSemesterPeriodsDTO {
    private Long semesterId;
    private Integer semesterNumber;
    private List<PeriodWithSubjectsDTO> periods;
}


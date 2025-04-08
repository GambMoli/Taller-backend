package co.edu.udes.backend.dto.academicRecord;

import co.edu.udes.backend.dto.subject.SubjectStatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicRecordDTO {
    private Long studentId;
    private String studentName;
    private String careerName;
    private Integer currentSemester;
    private List<SubjectStatusDTO> completedSubjects;
    private List<SubjectStatusDTO> inProgressSubjects;
    private List<SubjectStatusDTO> pendingSubjects;
}


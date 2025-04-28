package co.edu.udes.backend.dto.student;

import lombok.Data;

import java.util.List;

@Data
public class StudentSemesterDTO {
    private Long studentId;
    private String studentName;
    private List<SemesterInfoDTO> semesters;
    private Double averageGrade;
}

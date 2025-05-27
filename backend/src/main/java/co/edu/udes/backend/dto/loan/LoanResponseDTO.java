package co.edu.udes.backend.dto.loan;

import co.edu.udes.backend.dto.material.MaterialDTO;
import co.edu.udes.backend.dto.student.StudentDTO;
import co.edu.udes.backend.dto.teacher.TeacherDTO;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class LoanResponseDTO {
    private Long id;
    private String code;
    private MaterialDTO material;
    private TeacherDTO teacher;
    private StudentDTO student;
    private LocalDate loanDate;
    private LocalDate  deadline;
    private LocalDate   actualReturnDate;
    private String returnState;
    private String status;
}

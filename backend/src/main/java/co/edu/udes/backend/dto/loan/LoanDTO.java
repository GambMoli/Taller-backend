package co.edu.udes.backend.dto.loan;

import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LoanDTO {

    private Long id;
    private String code;
    private Long materialId;
    private Long teacherId;
    private Long studentId;
    private LocalDateTime   loanDate;
    private LocalDateTime   deadline;
    private LocalDateTime   actualReturnDate;
    private String returnState;
    private String status;

}

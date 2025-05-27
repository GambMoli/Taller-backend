package co.edu.udes.backend.dto.reserve;


import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.time.LocalTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDTO {
    private String code;
    private LocalDate reserveDate;
    private LocalTime hourInit;
    private LocalTime hourFinish;
    private String state;
    private Place placeId;
    private Teacher teacherId;
    private Student studentId;

}

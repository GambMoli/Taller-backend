package co.edu.udes.backend.dto.reserve;

import co.edu.udes.backend.dto.place.PlaceDTO;
import co.edu.udes.backend.dto.student.StudentDTO;
import co.edu.udes.backend.dto.teacher.TeacherDTO;

import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveResponseDTO {
    private String code;
    private LocalDate reserveDate;
    private LocalTime hourInit;
    private LocalTime hourFinish;
    private String state;
    private Place place;
    private Teacher teacher;
    private Student student;
}

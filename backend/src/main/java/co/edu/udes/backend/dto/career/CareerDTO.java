package co.edu.udes.backend.dto.career;

import co.edu.udes.backend.dto.semester.SemesterDTO;
import co.edu.udes.backend.dto.subject.SubjectDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CareerDTO {
    private Long id;
    private String name;
    private List<SemesterDTO> semesters;
}

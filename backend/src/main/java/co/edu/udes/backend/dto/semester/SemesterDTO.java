package co.edu.udes.backend.dto.semester;

import co.edu.udes.backend.dto.subject.SubjectDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SemesterDTO {
    private Long id;
    private Integer number;
    private List<SubjectDTO> subjects;
}

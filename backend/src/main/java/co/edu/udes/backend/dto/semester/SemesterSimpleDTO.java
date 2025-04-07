package co.edu.udes.backend.dto.semester;

import co.edu.udes.backend.models.Semester;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterSimpleDTO {
    private Long id;
    private Integer number;

    public static SemesterSimpleDTO fromEntity(Semester semester) {
        SemesterSimpleDTO dto = new SemesterSimpleDTO();
        dto.setId(semester.getId());
        dto.setNumber(semester.getNumber());
        return dto;
    }
}
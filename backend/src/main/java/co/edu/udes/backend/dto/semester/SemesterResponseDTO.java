package co.edu.udes.backend.dto.semester;

import co.edu.udes.backend.dto.career.CareerSimpleDTO;
import co.edu.udes.backend.dto.subject.SubjectSimpleDTO;
import co.edu.udes.backend.models.Semester;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SemesterResponseDTO {
    private Long id;
    private Integer number;
    private CareerSimpleDTO career;
    private List<SubjectSimpleDTO> subjects;

    public static SemesterResponseDTO fromEntity(Semester semester) {
        SemesterResponseDTO dto = new SemesterResponseDTO();
        dto.setId(semester.getId());
        dto.setNumber(semester.getNumber());

        if (semester.getCareer() != null) {
            dto.setCareer(CareerSimpleDTO.fromEntity(semester.getCareer()));
        }

        if (semester.getSubjects() != null && !semester.getSubjects().isEmpty()) {
            dto.setSubjects(semester.getSubjects().stream()
                    .map(SubjectSimpleDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
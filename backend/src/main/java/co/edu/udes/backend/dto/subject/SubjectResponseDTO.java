package co.edu.udes.backend.dto.subject;

import co.edu.udes.backend.dto.career.CareerSimpleDTO;
import co.edu.udes.backend.dto.groups.GroupClassSimpleDTO;
import co.edu.udes.backend.dto.semester.SemesterSimpleDTO;
import co.edu.udes.backend.models.Subject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SubjectResponseDTO {
    private Long id;
    private String name;
    private SemesterSimpleDTO semester;
    private List<CareerSimpleDTO> careers;
    private List<SubjectSimpleDTO> prerequisites;
    private List<GroupClassSimpleDTO> groups; // Agregamos los grupos

    public static SubjectResponseDTO fromEntity(Subject subject) {
        SubjectResponseDTO dto = new SubjectResponseDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());

        if (subject.getSemester() != null) {
            dto.setSemester(SemesterSimpleDTO.fromEntity(subject.getSemester()));
        }

        dto.setCareers(subject.getCareers().stream()
                .map(CareerSimpleDTO::fromEntity)
                .collect(Collectors.toList()));

        if (subject.getPrerequisites() != null && !subject.getPrerequisites().isEmpty()) {
            List<SubjectSimpleDTO> prerequisitesDTOs = subject.getPrerequisites().stream()
                    .map(SubjectSimpleDTO::fromEntity)
                    .collect(Collectors.toList());
            dto.setPrerequisites(prerequisitesDTOs);
        }

        // Agregamos los grupos si existen
        if (subject.getGroups() != null && !subject.getGroups().isEmpty()) {
            dto.setGroups(subject.getGroups().stream()
                    .map(GroupClassSimpleDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
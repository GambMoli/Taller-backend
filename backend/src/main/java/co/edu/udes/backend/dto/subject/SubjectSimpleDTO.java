package co.edu.udes.backend.dto.subject;

import co.edu.udes.backend.models.Subject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectSimpleDTO {
    private Long id;
    private String name;

    public static SubjectSimpleDTO fromEntity(Subject subject) {
        SubjectSimpleDTO dto = new SubjectSimpleDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        return dto;
    }
}

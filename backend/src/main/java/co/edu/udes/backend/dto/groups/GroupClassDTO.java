package co.edu.udes.backend.dto.groups;

import co.edu.udes.backend.dto.subject.SubjectSimpleDTO;
import co.edu.udes.backend.models.GroupClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupClassDTO {
    private Long id;
    private String name;
    private Integer capacity;
    private Long subjectId;
}

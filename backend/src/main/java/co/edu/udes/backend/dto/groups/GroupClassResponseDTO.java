package co.edu.udes.backend.dto.groups;

import co.edu.udes.backend.dto.subject.SubjectSimpleDTO;
import co.edu.udes.backend.models.GroupClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupClassResponseDTO {
    private Long id;
    private String name;
    private Integer capacity;
    private SubjectSimpleDTO subject;

    public static GroupClassResponseDTO fromEntity(GroupClass groupClass) {
        GroupClassResponseDTO dto = new GroupClassResponseDTO();
        dto.setId(groupClass.getId());
        dto.setName(groupClass.getName());
        dto.setCapacity(groupClass.getCapacity());

        if (groupClass.getSubject() != null) {
            dto.setSubject(SubjectSimpleDTO.fromEntity(groupClass.getSubject()));
        }

        return dto;
    }
}
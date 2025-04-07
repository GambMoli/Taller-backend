package co.edu.udes.backend.dto.groups;

import co.edu.udes.backend.models.GroupClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupClassSimpleDTO {
    private Long id;
    private String name;
    private Integer capacity;

    public static GroupClassSimpleDTO fromEntity(GroupClass groupClass) {
        GroupClassSimpleDTO dto = new GroupClassSimpleDTO();
        dto.setId(groupClass.getId());
        dto.setName(groupClass.getName());
        dto.setCapacity(groupClass.getCapacity());
        return dto;
    }
}
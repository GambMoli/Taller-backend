package co.edu.udes.backend.dto.career;

import co.edu.udes.backend.models.Career;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerSimpleDTO {
    private Long id;
    private String name;

    public static CareerSimpleDTO fromEntity(Career career) {
        CareerSimpleDTO dto = new CareerSimpleDTO();
        dto.setId(career.getId());
        dto.setName(career.getName());
        return dto;
    }
}
package co.edu.udes.backend.dto.semester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterCreateDTO {
    private Integer number;
    private Long careerId;
}
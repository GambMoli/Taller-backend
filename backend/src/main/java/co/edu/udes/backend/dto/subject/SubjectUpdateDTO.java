package co.edu.udes.backend.dto.subject;

import co.edu.udes.backend.models.Subject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectUpdateDTO {
    private String name;
    private List<Subject> prerequisites;
    private Long semesterId;  // Nuevo campo para actualizar semestre
}
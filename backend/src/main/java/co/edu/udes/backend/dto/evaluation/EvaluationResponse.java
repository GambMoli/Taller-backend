package co.edu.udes.backend.dto.evaluation;

import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationResponse {
    private Long id;
    private String name;
    private String fileName;
    private Long subjectId;
}
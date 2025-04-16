package co.edu.udes.backend.dto.evaluation;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class EvaluationDTO {
    private Long subjectId;
    private String name;
    private MultipartFile file;
}

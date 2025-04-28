package co.edu.udes.backend.mappers.evaluation;

import co.edu.udes.backend.dto.evaluation.EvaluationDTO;
import co.edu.udes.backend.dto.evaluation.EvaluationResponse;
import co.edu.udes.backend.models.Evaluation;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.repositories.SubjectRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class EvaluationMapper {

    private static SubjectRepository subjectRepository;

    @Autowired
    public void setSubjectRepository(SubjectRepository subjectRepository) {
        EvaluationMapper.subjectRepository = subjectRepository;
    }

    @Mapping(target = "fileName", expression = "java(dto.getFile().getOriginalFilename())")
    @Mapping(target = "data", expression = "java(dto.getFile().getBytes())")
    @Mapping(target = "subject", expression = "java(mapSubject(dto.getSubjectId()))") // Convierte subjectId a Subject
    public abstract Evaluation toEntity(EvaluationDTO dto) throws IOException;

    @Mapping(target = "subjectId", expression = "java(evaluation.getSubject().getId())") // Mapea subjectId desde el subject
    public abstract EvaluationResponse toResponse(Evaluation evaluation);

    // Método para mapear Long a Subject
    public static Subject mapSubject(Long subjectId) {
        if (subjectId == null) {
            return null;
        }
        // Usa el SubjectRepository para obtener el Subject por su ID
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }
}





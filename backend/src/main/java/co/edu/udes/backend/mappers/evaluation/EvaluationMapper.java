package co.edu.udes.backend.mappers.evaluation;


import co.edu.udes.backend.dto.evaluation.EvaluationDTO;
import co.edu.udes.backend.dto.evaluation.EvaluationResponse;
import co.edu.udes.backend.models.Evaluation;
import co.edu.udes.backend.repositories.SubjectRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = {SubjectRepository.class})
public interface EvaluationMapper {

    @Mapping(target = "subject", source = "subjectId")
    @Mapping(target = "fileName", expression = "java(dto.getFile().getOriginalFilename())")
    @Mapping(target = "data", expression = "java(dto.getFile().getBytes())")
    Evaluation toEntity(EvaluationDTO dto) throws IOException;

    EvaluationResponse toResponse(Evaluation evaluation);
}


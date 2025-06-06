package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.evaluation.EvaluationDTO;
import co.edu.udes.backend.dto.evaluation.EvaluationResponse;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.evaluation.EvaluationMapper;
import co.edu.udes.backend.models.Evaluation;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.repositories.EvaluationRepository;
import co.edu.udes.backend.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final EvaluationMapper evaluationMapper;
    private final SubjectRepository subjectRepository;

    public EvaluationService(EvaluationRepository evaluationRepository,
                             EvaluationMapper evaluationMapper,
                             SubjectRepository subjectRepository) {
        this.evaluationRepository = evaluationRepository;
        this.evaluationMapper = evaluationMapper;
        this.subjectRepository = subjectRepository;
    }


    public EvaluationResponse createEvaluation(EvaluationDTO evaluationDTO) throws IOException {
        Subject subject = subjectRepository.findById(evaluationDTO.getSubjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        Evaluation evaluation = evaluationMapper.toEntity(evaluationDTO);
        evaluation.setSubject(subject);

        try {
            evaluation.setFileName(evaluationDTO.getFile().getOriginalFilename());
            evaluation.setData(evaluationDTO.getFile().getBytes());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.NOT_CREATED_EVALUATION);
        }

        Evaluation saved = evaluationRepository.save(evaluation);

        return evaluationMapper.toResponse(saved);
    }

    public List<EvaluationResponse> getEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(evaluationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public byte[] getEvaluationFile(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        return evaluation.getData();
    }
}

package co.edu.udes.backend.mappers.grade;

import co.edu.udes.backend.dto.grade.GradeCreateDTO;
import co.edu.udes.backend.dto.grade.GradeDTO;
import co.edu.udes.backend.dto.grade.GradeResponseDTO;
import co.edu.udes.backend.models.Evaluation;
import co.edu.udes.backend.models.Grade;
import co.edu.udes.backend.models.Period;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.repositories.EvaluationRepository;
import co.edu.udes.backend.repositories.PeriodRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.repositories.SubjectRepository;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final PeriodRepository periodRepository;
    private final EvaluationRepository evaluationRepository;

    public GradeMapper(StudentRepository studentRepository,
                       SubjectRepository subjectRepository,
                       PeriodRepository periodRepository,
                       EvaluationRepository evaluationRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.periodRepository = periodRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public Grade toEntity(GradeCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Grade grade = new Grade();
        grade.setValue(dto.getValue());

        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId()).orElse(null);
            grade.setStudent(student);
        }

        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId()).orElse(null);
            grade.setSubject(subject);
        }

        if (dto.getPeriodId() != null) {
            Period period = periodRepository.findById(dto.getPeriodId()).orElse(null);
            grade.setPeriod(period);
        }

        if (dto.getEvaluationId() != null) {
            Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId()).orElse(null);
            grade.setEvaluation(evaluation);
        }

        return grade;
    }

    public GradeDTO toDTO(Grade entity) {
        if (entity == null) {
            return null;
        }

        GradeDTO dto = new GradeDTO();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());

        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
        }

        if (entity.getSubject() != null) {
            dto.setSubjectId(entity.getSubject().getId());
        }

        if (entity.getPeriod() != null) {
            dto.setPeriodId(entity.getPeriod().getId());
        }

        if (entity.getEvaluation() != null) {
            dto.setEvaluationId(entity.getEvaluation().getId());
        }

        return dto;
    }

    public GradeResponseDTO toResponseDTO(Grade entity) {
        if (entity == null) {
            return null;
        }

        GradeResponseDTO dto = new GradeResponseDTO();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());

        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
            dto.setStudentName(entity.getStudent().getName());
        }

        if (entity.getSubject() != null) {
            dto.setSubjectId(entity.getSubject().getId());
            dto.setSubjectName(entity.getSubject().getName());
        }

        if (entity.getPeriod() != null) {
            dto.setPeriodId(entity.getPeriod().getId());
            dto.setPeriodName(entity.getPeriod().getName());
        }

        if (entity.getEvaluation() != null) {
            dto.setEvaluationId(entity.getEvaluation().getId());
            dto.setEvaluationName(entity.getEvaluation().getName());
        }

        return dto;
    }
}
package co.edu.udes.backend.mappers.period;

import co.edu.udes.backend.dto.period.PeriodCreateDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.dto.period.PeriodUpdateDTO;
import co.edu.udes.backend.dto.period.SubjectGradeDTO;
import co.edu.udes.backend.models.Grade;
import co.edu.udes.backend.models.Period;
import co.edu.udes.backend.models.Student;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PeriodMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", source = "student")
    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "finalGrade", ignore = true)
    @Mapping(target = "name", source = "dto.name") // 👈 Soluciona la ambigüedad
    Period toEntity(PeriodCreateDTO dto, Student student);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.name")
    @Mapping(target = "subjectGrades", expression = "java(mapSubjectGrades(period))")
    @Mapping(target = "name", source = "name")
    PeriodResponseDTO toResponseDTO(Period period);


    default List<SubjectGradeDTO> mapSubjectGrades(Period period) {
        // Group grades by subject
        Map<Long, List<Grade>> gradesBySubject = period.getGrades().stream()
                .collect(Collectors.groupingBy(grade -> grade.getSubject().getId()));

        return gradesBySubject.entrySet().stream().map(entry -> {
            Long subjectId = entry.getKey();
            List<Grade> grades = entry.getValue();

            // Calculate average grade for the subject
            double avgGrade = grades.stream()
                    .mapToDouble(Grade::getValue)
                    .average()
                    .orElse(0.0);

            SubjectGradeDTO dto = new SubjectGradeDTO();
            dto.setSubjectId(subjectId);
            dto.setSubjectName(grades.get(0).getSubject().getName());
            dto.setGrade(avgGrade);
            dto.setApproved(avgGrade >= 3.0); // Assuming passing grade is 3.0

            return dto;
        }).collect(Collectors.toList());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePeriodFromDTO(PeriodUpdateDTO dto, @MappingTarget Period period);
}